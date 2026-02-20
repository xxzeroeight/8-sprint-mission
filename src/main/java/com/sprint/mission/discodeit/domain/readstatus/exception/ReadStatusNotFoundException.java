package com.sprint.mission.discodeit.domain.readstatus.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.ReadStatusException;

import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException
{
    public ReadStatusNotFoundException(UUID readStatusId) {
        super(ErrorCode.READSTATUS_NOT_FOUND, Map.of("readStatus", readStatusId));
    }
}
