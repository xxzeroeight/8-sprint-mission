package com.sprint.mission.discodeit.userstatus.exception;

import com.sprint.mission.discodeit.common.exception.DuplicateException;

import java.util.UUID;

public class DuplicateUserStatusException extends DuplicateException
{
    public DuplicateUserStatusException(String message) {
        super(message);
    }

    public static DuplicateUserStatusException byId(UUID id) {
        return new DuplicateUserStatusException("이미 존재하는 아이디입니다: ID=" + id);
    }
}
