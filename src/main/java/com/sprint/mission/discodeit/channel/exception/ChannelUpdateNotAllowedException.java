package com.sprint.mission.discodeit.channel.exception;

public class ChannelUpdateNotAllowedException extends RuntimeException
{
    public ChannelUpdateNotAllowedException(String message) {
        super(message);
    }

    public static ChannelUpdateNotAllowedException forPrivateChannel(String message) {
        return new ChannelUpdateNotAllowedException(message);
    }
}
