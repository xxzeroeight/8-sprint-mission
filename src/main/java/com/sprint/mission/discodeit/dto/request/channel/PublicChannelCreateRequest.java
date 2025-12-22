package com.sprint.mission.discodeit.dto.request.channel;

public record PublicChannelCreateRequest(
        String channelName,
        String description
) {}
