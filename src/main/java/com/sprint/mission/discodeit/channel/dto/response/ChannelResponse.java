package com.sprint.mission.discodeit.channel.dto.response;

import com.sprint.mission.discodeit.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.channel.dto.domain.ChannelDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        String name,
        String description,
        ChannelType type,
        List<UUID> participantIds,
        Instant lastMessageAt,

        Instant createdAt,
        Instant updatedAt
) {
    public static ChannelResponse from(ChannelDto channel) {
        return new ChannelResponse(
                channel.id(),
                channel.channelName(),
                channel.description(),
                channel.channelType(),
                channel.userIds(),
                channel.lastMessageAt(),
                channel.createdAt(),
                channel.udpatedAt()
        );
    }
}
