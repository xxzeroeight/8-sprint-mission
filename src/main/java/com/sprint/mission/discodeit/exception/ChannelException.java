package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class ChannelException extends RuntimeException
{
    public ChannelException(String message) {
        super(message);
    }

    public static class ChannelNotFoundException extends ChannelException {
        public ChannelNotFoundException(UUID id) {
            super(id + "채널을 찾을 수 없습니다.");
        }
    }
}
