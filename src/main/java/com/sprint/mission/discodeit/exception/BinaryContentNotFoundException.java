package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class BinaryContentNotFoundException extends RuntimeException
{
    public BinaryContentNotFoundException(String message) {
        super(message);
    }

    public static BinaryContentNotFoundException byId(UUID id) {
        return new BinaryContentNotFoundException("첨부 파일이 존재하지 않습니다: ID=" + id);
    }
}
