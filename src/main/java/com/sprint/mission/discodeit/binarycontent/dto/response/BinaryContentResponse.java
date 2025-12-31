package com.sprint.mission.discodeit.binarycontent.dto.response;

import com.sprint.mission.discodeit.binarycontent.dto.domain.BinaryContentDto;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        String bytes,

        Instant createdAt
) {
    public static BinaryContentResponse from(BinaryContentDto binaryContent) {
        String bytes = Base64.getEncoder().encodeToString(binaryContent.bytes());

        return new BinaryContentResponse(
                binaryContent.id(),
                binaryContent.fileName(),
                binaryContent.contentType(),
                binaryContent.size(),
                bytes,
                binaryContent.createdAt()
        );
    }
}