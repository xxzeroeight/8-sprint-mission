package com.sprint.mission.discodeit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprint.mission.discodeit.dto.entity.ReadStatusDto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        UUID userId,
        UUID channelId,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant updatedAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant lastReadAt
) {
    public static ReadStatusResponse from(ReadStatusDto readStatus) {
        return new ReadStatusResponse(
                readStatus.id(),
                readStatus.userId(),
                readStatus.channelId(),
                readStatus.createdAt(),
                readStatus.updatedAt(),
                readStatus.lasdReadAt()
        );
    }
}
