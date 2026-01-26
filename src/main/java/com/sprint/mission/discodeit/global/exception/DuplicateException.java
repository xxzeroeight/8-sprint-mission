package com.sprint.mission.discodeit.global.exception;

public abstract class DuplicateException extends RuntimeException
{
    public DuplicateException(String message) {
        super(message);
    }
}
