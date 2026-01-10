package com.sprint.mission.discodeit.domain.binarycontent.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final UUID id;

    private String fileName;
    private Long size;
    private String contentType;
    private byte[] bytes;

    private Instant createdAt;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
        this.createdAt = Instant.now();
    }
}
