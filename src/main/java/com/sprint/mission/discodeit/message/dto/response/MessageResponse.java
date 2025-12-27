package com.sprint.mission.discodeit.message.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprint.mission.discodeit.message.dto.domain.MessageDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds,
        String content,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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
