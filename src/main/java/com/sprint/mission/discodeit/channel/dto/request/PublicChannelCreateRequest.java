package com.sprint.mission.discodeit.channel.dto.request;

public record PublicChannelCreateRequest(
        String channelName,
        String description
) {}
