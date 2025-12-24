package com.sprint.mission.discodeit.exception;

public class InvalidPasswordException extends RuntimeException
{
    public InvalidPasswordException(String message) {
        super(message);
    }

    public static InvalidPasswordException incorrect() {
        return new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
    }
}
