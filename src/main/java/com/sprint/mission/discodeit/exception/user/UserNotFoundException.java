package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.NotFoundException;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException
{
    private UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byId(UUID id) {
        return new UserNotFoundException("유저를 찾을 수 없습니다: ID=" + id);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("유저를 찾을 수 없습니다: Email=" + email);
    }

    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException("유저를 찾을 수 없습니다: Username=" + username);
    }
}
