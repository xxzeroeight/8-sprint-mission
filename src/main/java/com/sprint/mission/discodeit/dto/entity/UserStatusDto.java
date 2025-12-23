package com.sprint.mission.discodeit.dto.entity;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID id,
        UUID userId,
        Instant createdAt,
        Instant updatedAt,
        Instant lastActiveAt
) {
}
