package com.sprint.mission.discodeit.domain.channel.dto.domain;

import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,

        String name,
        String description,
        ChannelType type,
        List<UserDto> participants,

        Instant lastMessageAt
) {}
