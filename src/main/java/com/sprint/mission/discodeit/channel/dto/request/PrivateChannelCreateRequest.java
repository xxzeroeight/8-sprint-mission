package com.sprint.mission.discodeit.channel.dto.request;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        List<UUID> userIds
) {}
