package com.sprint.mission.discodeit.global.secutiry.auth.exception;

public class InvalidPasswordException extends RuntimeException
{
    public InvalidPasswordException(String message) {
        super(message);
    }

    public static InvalidPasswordException incorrect() {
        return new InvalidPasswordException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}
