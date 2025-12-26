package com.sprint.mission.discodeit.dto.entity;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String channelName,
        String description,
        ChannelType channelType,
        List<UUID> userIds,
        Instant lastMessageAt,
        Instant createdAt,
        Instant udpatedAt
) {
    public static ChannelDto from(Channel channel, List<UUID> userIds, Instant lastMessageAt) {
        return new ChannelDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getChannelType(),
                userIds,
                lastMessageAt,
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }
}
