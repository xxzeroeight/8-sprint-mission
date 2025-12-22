package com.sprint.mission.discodeit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprint.mission.discodeit.dto.entity.ChannelDto;
import com.sprint.mission.discodeit.entity.enums.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        String channelName,
        String description,
        ChannelType channelType,
        List<UUID> userIds,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant updatedAt
) {
    public static ChannelResponse from(ChannelDto channel) {
        return new ChannelResponse(
                channel.id(),
                channel.channelName(),
                channel.description(),
                channel.channelType(),
                channel.userIds(),
                channel.createdAt(),
                channel.udpatedAt()
        );
    }
}
