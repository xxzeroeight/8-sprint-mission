package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.UserException;

import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException
{
    public UserNotFoundException(UUID userId) {
        super(ErrorCode.USER_NOT_FOUND, Map.of("user", userId));
    }

    public UserNotFoundException(String username) {
        super(ErrorCode.USER_NOT_FOUND, Map.of("user", username));
    }
}
