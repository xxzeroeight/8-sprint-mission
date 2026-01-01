package com.sprint.mission.discodeit.common.exception;

public abstract class NotFoundException extends RuntimeException
{
    public NotFoundException(String message) {
        super(message);
    }
}
