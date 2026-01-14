package com.sprint.mission.discodeit.domain.binarycontent.dto.domain;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        String contentType,
        Long size
) {}
