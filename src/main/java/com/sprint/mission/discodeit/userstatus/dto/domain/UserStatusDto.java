package com.sprint.mission.discodeit.userstatus.dto.domain;

import com.sprint.mission.discodeit.userstatus.domain.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID id,
        UUID userId,
        Instant createdAt,
        Instant updatedAt,
        Instant lastActiveAt
) {
    public static UserStatusDto from(UserStatus userStatus) {
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt(),
                userStatus.getLastActiveAt()
        );
    }
}
