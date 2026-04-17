package com.sprint.mission.discodeit.domain.readstatus.dto.request;

import java.time.Instant;

public record ReadStatusUpdateRequest
(
        Instant newLastReadAt,
        Boolean newNotificationEnabled
) {}
