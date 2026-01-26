package com.sprint.mission.discodeit.domain.message.dto.request;

import java.util.UUID;

public record MessageCreateRequest(
        UUID channelId,
        UUID authorId,
        String content
) {}
