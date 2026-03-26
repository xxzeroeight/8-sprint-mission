package com.sprint.mission.discodeit.domain.user.mapper;

import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserMapper
{
    private final SessionRegistry sessionRegistry;
    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user) {
        BinaryContentDto profile = user.getProfile() != null ? binaryContentMapper.toDto(user.getProfile()) : null;

        boolean online = isOnline(user.getId());

        return new UserDto(
                user.getId(),
                profile,
                user.getUsername(),
                user.getEmail(),
                online,
                user.getRole()
        );
    }

    public boolean isOnline(UUID userId) {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof DiscodeitUserDetails)
                .map(principal -> (DiscodeitUserDetails) principal)
                .anyMatch(userDetails -> userDetails.getUserResponse().id().equals(userId));
    }
}
