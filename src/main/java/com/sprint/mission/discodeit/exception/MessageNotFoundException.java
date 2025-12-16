package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class MessageNotFoundException extends RuntimeException
{
    public MessageNotFoundException(String message) {
        super(message);
    }

    public static MessageNotFoundException byId(UUID id) {
        return new MessageNotFoundException("메시지를 찾을 수 없습니다: ID=" + id);
    }
}
