package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class UserStatusNotFoundException extends RuntimeException
{
    public UserStatusNotFoundException(String message) {
        super(message);
    }

    public static UserStatusNotFoundException byId(UUID id) {
        return new UserStatusNotFoundException("유저 상태를 찾을 수 없습니다: ID=" + id);
    }
}
