package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
@Repository
public class JCFUserStatusRepository implements UserStatusRepository
{
    private final Map<UUID, UserStatus> data = new HashMap<>();

    private JCFUserStatusRepository() {}

    private static JCFUserStatusRepository instance;
    public static JCFUserStatusRepository getInstance() {
        if (instance == null) instance = new JCFUserStatusRepository();
        return instance;
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);

        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        UserStatus userStatus = data.get(id);

        return Optional.ofNullable(userStatus);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> userStatuses = data.values().stream().toList();

        return userStatuses;
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findByUserId(userId)
                .ifPresent(userStatus -> data.remove(userStatus.getId()));
    }
}
