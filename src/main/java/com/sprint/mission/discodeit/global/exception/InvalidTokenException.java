package com.sprint.mission.discodeit.global.exception;

import java.util.Map;

public class InvalidTokenException extends AuthException
{
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN, Map.of());
    }

    public InvalidTokenException(String message) {
        super(ErrorCode.INVALID_TOKEN, Map.of("message", message));
    }
}
