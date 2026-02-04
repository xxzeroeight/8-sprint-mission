package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.UserException;

import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends UserException
{
    public ChannelNotFoundException(UUID channelId) {
        super(ErrorCode.CHANNEL_NOT_FOUND, Map.of("channel", channelId));
    }
}
