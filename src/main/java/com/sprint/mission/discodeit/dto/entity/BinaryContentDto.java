package com.sprint.mission.discodeit.dto.entity;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        byte[] bytes,
        Instant createdAt
) {}
