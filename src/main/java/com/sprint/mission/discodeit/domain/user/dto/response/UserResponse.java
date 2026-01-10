package com.sprint.mission.discodeit.domain.user.dto.response;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        UUID profileId,
        String username,
        String email,
        boolean online,

        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse from(UserDto user) {
        return new UserResponse(
                user.id(),
                user.profileId(),
                user.username(),
                user.email(),
                user.online(),
                user.createdAt(),
                user.updatedAt()
        );
    }
}
