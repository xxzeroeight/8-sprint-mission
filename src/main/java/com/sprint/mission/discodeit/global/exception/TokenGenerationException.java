package com.sprint.mission.discodeit.global.exception;

import java.util.Map;

public class TokenGenerationException extends AuthException
{
    public TokenGenerationException() {
        super(ErrorCode.TOKEN_GENERATION_FAILED, Map.of());
    }
}
