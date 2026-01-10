package com.sprint.mission.discodeit.domain.message.exception;

import com.sprint.mission.discodeit.global.exception.NotFoundException;

import java.util.UUID;

public class MessageNotFoundException extends NotFoundException
{
    public MessageNotFoundException(String message) {
        super(message);
    }

    public static MessageNotFoundException byId(UUID id) {
        return new MessageNotFoundException("메시지를 찾을 수 없습니다: ID=" + id);
    }
}
