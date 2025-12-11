package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class UserException extends RuntimeException
{
    public UserException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends UserException {
        public UserNotFoundException(UUID id) {
            super(id + "유저를 찾을 수 없습니다.");
        }
    }
}
