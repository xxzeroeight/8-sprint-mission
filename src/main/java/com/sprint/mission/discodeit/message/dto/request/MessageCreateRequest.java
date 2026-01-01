package com.sprint.mission.discodeit.message.dto.request;

import java.util.UUID;

public record MessageCreateRequest(
        UUID channelId,
        UUID authorId,
        String content
) {}
