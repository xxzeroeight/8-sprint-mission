package com.sprint.mission.discodeit.domain.channel.dto.response;

import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,

        String name,
        String description,
        ChannelType type,
        List<UserDto> participants,

        Instant lastMessageAt
) {
    public static ChannelResponse from(ChannelDto channel) {
        return new ChannelResponse(
                channel.id(),
                channel.name(),
                channel.description(),
                channel.type(),
                channel.participants(),
                channel.lastMessageAt()
        );
    }
}
