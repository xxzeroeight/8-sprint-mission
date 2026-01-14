package com.sprint.mission.discodeit.domain.message.dto.response;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID channelId,

        UserDto author,
        List<BinaryContentDto> attachments,
        String content,

        Instant createdAt,
        Instant updatedAt
) {
    public static MessageResponse from(MessageDto messageDto) {
        return new MessageResponse(
                messageDto.id(),
                messageDto.channelId(),
                messageDto.author(),
                messageDto.attachments(),
                messageDto.content(),
                messageDto.createdAt(),
                messageDto.updatedAt()
        );
    }
}
