package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.discodeit.common.exception.NotFoundException;

import java.util.UUID;

public class BinaryContentNotFoundException extends NotFoundException
{
    public BinaryContentNotFoundException(String message) {
        super(message);
    }

    public static BinaryContentNotFoundException byId(UUID id) {
        return new BinaryContentNotFoundException("첨부 파일이 존재하지 않습니다: ID=" + id);
    }
}
