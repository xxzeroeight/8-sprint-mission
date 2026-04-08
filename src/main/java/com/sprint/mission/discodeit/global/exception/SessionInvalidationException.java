package com.sprint.mission.discodeit.global.exception;

import java.util.Map;

public class SessionInvalidationException extends AuthException
{
    public SessionInvalidationException(String data) {
        super(ErrorCode.SESSION_INVALIDATION_FAILED, Map.of("data", data));
    }
}
