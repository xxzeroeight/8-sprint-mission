package com.sprint.mission.discodeit.readstatus.dto.response;

import com.sprint.mission.discodeit.readstatus.dto.domain.ReadStatusDto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        UUID userId,
        UUID channelId,

        Instant createdAt,
        Instant updatedAt,
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
