package com.sprint.mission.discodeit.global.secutiry;

import com.sprint.mission.discodeit.auth.dto.info.JwtInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
public class InMemoryJwtRegistry implements JwtRegistry
{
    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
    private final Set<String> accessTokenIndexes = ConcurrentHashMap.newKeySet();
    private final Set<String> refreshTokenIndexes = ConcurrentHashMap.newKeySet();

    private final int maxActiveJwtCount;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        origin.compute(jwtInformation.userDto().id(), (key, queue) -> {
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<>();
            }

            if (queue.size() >= maxActiveJwtCount) {
                JwtInformation deprecatedJwtInformation = queue.poll();
                if (deprecatedJwtInformation != null) {
                    removeTokenIndex(
                            deprecatedJwtInformation.accessToken(),
                            deprecatedJwtInformation.refreshToken()
                    );
                }
            }
            queue.add(jwtInformation); // Add the new token
            addTokenIndex(
                    jwtInformation.accessToken(),
                    jwtInformation.refreshToken()
            );
            return queue;
        });
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
        origin.computeIfPresent(userId, (key, queue) -> {
            queue.forEach(jwtInformation -> {
                removeTokenIndex(
                        jwtInformation.accessToken(),
                        jwtInformation.refreshToken()
                );
            });
            queue.clear();
            return null;
        });
    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        return origin.containsKey(userId);
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        return accessTokenIndexes.contains(accessToken);
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return refreshTokenIndexes.contains(refreshToken);
    }

    @Override
    public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {
        origin.computeIfPresent(newJwtInformation.userDto().id(), (key, queue) -> {
            queue.stream().filter(jwtInformation -> jwtInformation.refreshToken().equals(refreshToken))
                    .findFirst()
                    .ifPresent(jwtInformation -> {
                        removeTokenIndex(jwtInformation.accessToken(), jwtInformation.refreshToken());
                        jwtInformation.rotate(
                                newJwtInformation.accessToken(),
                                newJwtInformation.refreshToken()
                        );
                        addTokenIndex(
                                newJwtInformation.accessToken(),
                                newJwtInformation.refreshToken()
                        );
                    });
            return queue;
        });
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @Override
    public void clearExpiredJwtInformation() {
        origin.entrySet().removeIf(entry -> {
            Queue<JwtInformation> queue = entry.getValue();
            queue.removeIf(jwtInformation -> {
                boolean isExpired =
                        !jwtTokenProvider.validateAccessToken(jwtInformation.accessToken()) ||
                                !jwtTokenProvider.validateRefreshToken(jwtInformation.refreshToken());
                if (isExpired) {
                    removeTokenIndex(
                            jwtInformation.accessToken(),
                            jwtInformation.refreshToken()
                    );
                }
                return isExpired;
            });
            return queue.isEmpty();
        });
    }

    private void addTokenIndex(String accessToken, String refreshToken) {
        accessTokenIndexes.add(accessToken);
        refreshTokenIndexes.add(refreshToken);
    }

    private void removeTokenIndex(String accessToken, String refreshToken) {
        accessTokenIndexes.remove(accessToken);
        refreshTokenIndexes.remove(refreshToken);
    }
}
