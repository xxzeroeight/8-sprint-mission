package com.sprint.mission.discodeit.binarycontent.dto.request;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {}
