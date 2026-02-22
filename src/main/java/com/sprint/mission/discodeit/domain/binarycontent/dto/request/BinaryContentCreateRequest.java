package com.sprint.mission.discodeit.domain.binarycontent.dto.request;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {}
