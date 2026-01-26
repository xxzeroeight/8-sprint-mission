package com.sprint.mission.discodeit.domain.binarycontent.dto.response;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import org.springframework.core.io.Resource;

public record BinaryContentDownloadResponse(
        Resource resource,
        String fileName,
        String contentType,
        Long size
) {
    public static BinaryContentDownloadResponse from(Resource resource, BinaryContentDto binaryContent) {
        return new BinaryContentDownloadResponse(
                resource,
                binaryContent.fileName(),
                binaryContent.contentType(),
                binaryContent.size()
        );
    }
}
