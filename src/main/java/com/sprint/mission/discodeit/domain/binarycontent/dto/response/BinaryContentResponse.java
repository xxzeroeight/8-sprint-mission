package com.sprint.mission.discodeit.domain.binarycontent.dto.response;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContentStatus;
import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;

import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        BinaryContentStatus status
) {
    public static BinaryContentResponse from(BinaryContentDto binaryContent) {
        return new BinaryContentResponse(
                binaryContent.id(),
                binaryContent.fileName(),
                binaryContent.contentType(),
                binaryContent.size(),
                binaryContent.status()
        );
    }
}