package com.sprint.mission.discodeit.userstatus.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record UserStatusUpdateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant updateLastActiveAt
) {}
