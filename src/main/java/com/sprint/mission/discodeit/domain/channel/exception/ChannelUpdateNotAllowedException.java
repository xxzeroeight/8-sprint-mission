package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.exception.ChannelException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class ChannelUpdateNotAllowedException extends ChannelException
{
    public ChannelUpdateNotAllowedException(UUID channelId) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE, Map.of("channel", channelId));
    }
}
