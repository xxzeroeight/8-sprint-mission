package com.sprint.mission.discodeit.dto.request.readStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record ReadStatusUpdateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant updateLastReadAt
) {}
