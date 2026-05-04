package com.sprint.mission.discodeit.domain.binarycontent.dto.domain;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContentStatus;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        BinaryContentStatus status
) {}
