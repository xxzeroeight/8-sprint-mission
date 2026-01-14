package com.sprint.mission.discodeit.domain.message.dto.domain;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        UUID channelId,

        UserDto author,
        List<BinaryContentDto> attachments,
        String content,

        Instant createdAt,
        Instant updatedAt
) {}
