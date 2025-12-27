package com.sprint.mission.discodeit.readstatus.exception;

import com.sprint.mission.discodeit.common.exception.NotFoundException;

import java.util.UUID;

public class ReadStatusNotFoundException extends NotFoundException
{
    public ReadStatusNotFoundException(String message) {
        super(message);
    }

    public static ReadStatusNotFoundException byId(UUID id) {
        return new ReadStatusNotFoundException("상태를 찾을 수 없습니다: ID=" + id);
    }
}
