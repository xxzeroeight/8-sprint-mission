package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.UserException;

import java.util.Map;

public class UserAlreadyExistsException extends UserException
{
    public UserAlreadyExistsException(String value) {
        super(ErrorCode.DUPLICATE_USER, Map.of("value", value));
    }
}
