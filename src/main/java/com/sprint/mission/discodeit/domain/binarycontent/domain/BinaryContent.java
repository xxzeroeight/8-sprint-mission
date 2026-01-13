package com.sprint.mission.discodeit.domain.binarycontent.domain;

import com.sprint.mission.discodeit.domain.BaseEntity;
import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity
{
    private String fileName;
    private Long size;
    private String contentType;
    private byte[] bytes;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
