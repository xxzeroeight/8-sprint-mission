package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.NotFoundException;

import java.util.UUID;

public class ChannelNotFoundException extends NotFoundException
{
    public ChannelNotFoundException(String message) {
        super(message);
    }

    public static ChannelNotFoundException byId(UUID id) {
        return new ChannelNotFoundException("채널을 찾을 수 없습니다: ID=" + id);
    }
}
