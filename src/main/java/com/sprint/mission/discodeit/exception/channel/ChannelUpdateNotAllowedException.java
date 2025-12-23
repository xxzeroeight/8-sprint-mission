package com.sprint.mission.discodeit.exception.channel;

public class ChannelUpdateNotAllowedException extends RuntimeException
{
    public ChannelUpdateNotAllowedException(String message) {
        super(message);
    }

    public static ChannelUpdateNotAllowedException forPrivateChannel(String message) {
        return new ChannelUpdateNotAllowedException(message);
    }
}
