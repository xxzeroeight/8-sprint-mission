package com.sprint.mission.discodeit.dto.entity;

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
) {}
