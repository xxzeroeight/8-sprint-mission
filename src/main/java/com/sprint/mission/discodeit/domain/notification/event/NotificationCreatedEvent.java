package com.sprint.mission.discodeit.domain.notification.event;

import com.sprint.mission.discodeit.domain.notification.dto.entity.NotificationDto;

import java.time.Instant;
import java.util.List;

public record NotificationCreatedEvent
(
        String eventName,
        NotificationDto notification,
        List<NotificationDto> data,
        Instant createdAt
) {}
