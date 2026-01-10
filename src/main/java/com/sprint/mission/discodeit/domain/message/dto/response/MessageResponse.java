package com.sprint.mission.discodeit.domain.message.dto.response;

import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds,
        String content,

        Instant createdAt,
        Instant updatedAt
) {
    public static MessageResponse from(MessageDto message) {
        return new MessageResponse(
                message.id(),
                message.channelId(),
                message.authorId(),
                message.attachmentIds(),
                message.content(),
                message.createdAt(),
                message.updatedAt()
        );
    }
}
