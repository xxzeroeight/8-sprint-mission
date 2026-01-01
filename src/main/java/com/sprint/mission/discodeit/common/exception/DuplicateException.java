package com.sprint.mission.discodeit.common.exception;

public abstract class DuplicateException extends RuntimeException
{
    public DuplicateException(String message) {
        super(message);
    }
}
