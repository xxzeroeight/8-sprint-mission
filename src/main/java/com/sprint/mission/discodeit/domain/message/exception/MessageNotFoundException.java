package com.sprint.mission.discodeit.domain.message.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.MessageException;

import java.util.Map;
import java.util.UUID;

public class MessageNotFoundException extends MessageException
{
    public MessageNotFoundException(UUID messageId) {
        super(ErrorCode.MESSAGE_NOT_FOUND, Map.of("message", messageId));
    }
}
