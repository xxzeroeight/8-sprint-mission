package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class MessageException extends RuntimeException
{
    public MessageException(String message) {
        super(message);
    }

    public static class MessageNotFoundException extends MessageException {
        public MessageNotFoundException(UUID id) {
            super(id + "메시지를 찾을 수 없습니다.");
        }
    }
}
