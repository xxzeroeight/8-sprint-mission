package com.sprint.mission.discodeit.dto.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds,
        String content,
        Instant createdAt,
        Instant updatedAt
) {}
