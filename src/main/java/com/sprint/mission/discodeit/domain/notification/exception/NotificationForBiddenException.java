package com.sprint.mission.discodeit.domain.notification.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class NotificationForBiddenException extends NotificationException
{
    public NotificationForBiddenException(UUID receiverId) {
        super(ErrorCode.NOTIFICATION_FORBIDDEN, Map.of("receiverId", receiverId));
    }
}
