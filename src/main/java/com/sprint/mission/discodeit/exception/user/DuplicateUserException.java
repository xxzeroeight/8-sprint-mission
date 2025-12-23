package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DuplicateException;

import java.util.UUID;

public class DuplicateUserException extends DuplicateException
{
    public DuplicateUserException(String message) {
        super(message);
    }

    public static DuplicateUserException byUsername(String username) {
        return new DuplicateUserException("이미 존재하는 이름입니다: " + username);
    }

    public static DuplicateUserException byEmail(String email) {
        return new DuplicateUserException("이미 존재하는 이메일입니다: " + email);
    }

    public static DuplicateUserException byId(UUID id) {
        return new DuplicateUserException("이미 존재하는 아이디입니다: " + id);
    }
}
