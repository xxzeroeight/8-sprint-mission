package com.sprint.mission.discodeit.dto.entity;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static MessageDto from(Message message) {
        return new MessageDto(
                message.getId(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getAttachmentIds(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
