package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.DuplicateException;

import java.util.UUID;

public class DuplicateChannelException extends DuplicateException
{
    public DuplicateChannelException(String message) {
        super(message);
    }

    public static DuplicateChannelException byId(UUID id) {
        return new DuplicateChannelException("이미 존재하는 채널입니다: " + id);
    }
}
