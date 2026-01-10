package com.sprint.mission.discodeit.domain.userstatus.dto.response;

import com.sprint.mission.discodeit.domain.userstatus.dto.domain.UserStatusDto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
        UUID id,
        UUID userId,

        Instant createdAt,
        Instant updatedAt,
        Instant lastActiveAt
) {
    public static UserStatusResponse from(UserStatusDto userStatus) {
        return new UserStatusResponse(
                userStatus.id(),
                userStatus.userId(),
                userStatus.createdAt(),
                userStatus.updatedAt(),
                userStatus.lastActiveAt()
        );
    }
}
