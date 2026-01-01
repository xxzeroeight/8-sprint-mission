package com.sprint.mission.discodeit.readstatus.exception;

import com.sprint.mission.discodeit.common.exception.DuplicateException;

public class DuplicateReadStatusException extends DuplicateException
{
    public DuplicateReadStatusException(String message) {
        super(message);
    }
}
