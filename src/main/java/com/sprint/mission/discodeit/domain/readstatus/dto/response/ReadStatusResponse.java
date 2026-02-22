package com.sprint.mission.discodeit.domain.readstatus.dto.response;

import com.sprint.mission.discodeit.domain.readstatus.dto.domain.ReadStatusDto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        UUID userId,
        UUID channelId,

        Instant lastReadAt
) {
    public static ReadStatusResponse from(ReadStatusDto readStatus) {
        return new ReadStatusResponse(
                readStatus.id(),
                readStatus.userId(),
                readStatus.channelId(),
                readStatus.lastReadAt()
        );
    }
}
