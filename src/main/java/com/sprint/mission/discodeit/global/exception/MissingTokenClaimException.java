package com.sprint.mission.discodeit.global.exception;

import java.util.Map;

public class MissingTokenClaimException extends AuthException
{
    public MissingTokenClaimException() {
        super(ErrorCode.MISSING_TOKEN_CLAIM, Map.of());
    }
}
