package com.sprint.mission.discodeit.auth.exception;

import com.sprint.mission.discodeit.global.exception.AuthException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class PasswordMismatchException extends AuthException
{
    public PasswordMismatchException() {
        super(ErrorCode.PASSWORD_MISMATCH, Map.of());
    }
}
