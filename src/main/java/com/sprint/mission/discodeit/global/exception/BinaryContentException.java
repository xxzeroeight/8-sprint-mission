package com.sprint.mission.discodeit.global.exception;

import java.util.Map;

public class BinaryContentException extends DiscodeitException
{
    public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
