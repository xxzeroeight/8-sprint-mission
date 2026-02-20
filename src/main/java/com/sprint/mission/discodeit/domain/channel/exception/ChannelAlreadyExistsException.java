package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.exception.ChannelException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelAlreadyExistsException extends ChannelException
{
    public ChannelAlreadyExistsException(String value) {
        super(ErrorCode.DUPLICATE_CHANNEL, Map.of("value", value));
    }
}
