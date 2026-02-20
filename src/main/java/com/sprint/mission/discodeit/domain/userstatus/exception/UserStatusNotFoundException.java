package com.sprint.mission.discodeit.domain.userstatus.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.UserStatusException;

import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException
{
    public UserStatusNotFoundException(UUID userId) {
        super(ErrorCode.USERSTATUS_NOT_FOUND, Map.of("user", userId));
    }
}
