package com.sprint.mission.discodeit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprint.mission.discodeit.dto.entity.UserStatusDto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
        UUID id,
        UUID userId,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant updatedAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant lastActiveAt
) {
    public static UserStatusResponse from(UserStatusDto userStatus) {
        return new UserStatusResponse(
                userStatus.id(),
                userStatus.userId(),
                userStatus.createdAt(),
                userStatus.updatedAt(),
                userStatus.lastActiveAt()
        );
    }
}
