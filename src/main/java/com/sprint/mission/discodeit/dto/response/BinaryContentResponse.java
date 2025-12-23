package com.sprint.mission.discodeit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprint.mission.discodeit.dto.entity.BinaryContentDto;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        String base64Bytes,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant createdAt
) {
    public static BinaryContentResponse from(BinaryContentDto binaryContent) {
        String base64Bytes = Base64.getEncoder().encodeToString(binaryContent.bytes());

        return new BinaryContentResponse(
                binaryContent.id(),
                binaryContent.fileName(),
                binaryContent.contentType(),
                binaryContent.size(),
                base64Bytes,
                binaryContent.createdAt()
        );
    }
}