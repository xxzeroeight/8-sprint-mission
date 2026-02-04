package com.sprint.mission.discodeit.domain.readstatus.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.ReadStatusException;

import java.util.Map;
import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException
{
    public ReadStatusAlreadyExistsException(UUID channelId, UUID userId) {
        super(ErrorCode.DUPLICATE_READSTATUS, Map.of("channel", channelId, "user", userId));
    }
}