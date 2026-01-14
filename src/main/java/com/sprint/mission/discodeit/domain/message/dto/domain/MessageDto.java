package com.sprint.mission.discodeit.domain.message.dto.domain;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        UUID channelId,
        UserDto authorId,
        List<BinaryContentDto> attachmentIds,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static MessageDto from(Message message) {
        return new MessageDto(
                message.getId(),
                message.getChannel().getId(),
                UserDto.from(message.getAuthor(), message.getAuthor().getUserStatus().isOnline()),
                message.getAttachments() != null ? message.getAttachments().stream()
                        .map(BinaryContentDto::from)
                        .toList()
                        : List.of(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
