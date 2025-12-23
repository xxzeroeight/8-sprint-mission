package com.sprint.mission.discodeit.dto.request.userstatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record UserStatusUpdateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant updateLastActiveAt
) {}
