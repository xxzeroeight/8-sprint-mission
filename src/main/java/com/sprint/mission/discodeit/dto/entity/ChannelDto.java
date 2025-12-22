package com.sprint.mission.discodeit.dto.entity;

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
) {}
