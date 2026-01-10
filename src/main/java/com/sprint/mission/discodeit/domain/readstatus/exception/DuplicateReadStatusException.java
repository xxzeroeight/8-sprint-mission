package com.sprint.mission.discodeit.domain.readstatus.exception;

import com.sprint.mission.discodeit.global.exception.DuplicateException;

public class DuplicateReadStatusException extends DuplicateException
{
    public DuplicateReadStatusException(String message) {
        super(message);
    }
}
