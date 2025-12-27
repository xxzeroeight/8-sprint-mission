package com.sprint.mission.discodeit.binarycontent.dto.domain;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        byte[] bytes,
        Instant createdAt
) {
    public static BinaryContentDto from(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getSize(),
                binaryContent.getBytes(),
                binaryContent.getCreatedAt()
        );
    }
}
