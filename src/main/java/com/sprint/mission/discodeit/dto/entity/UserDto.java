package com.sprint.mission.discodeit.dto.entity;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        UUID profileId,
        String username,
        String email,
        Boolean online,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserDto from(User user, Boolean online) {
        return new UserDto(
                user.getId(),
                user.getProfileId(),
                user.getUsername(),
                user.getEmail(),
                online,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
