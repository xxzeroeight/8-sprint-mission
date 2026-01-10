package com.sprint.mission.discodeit.domain.userstatus.dto.request;

import java.time.Instant;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {}
