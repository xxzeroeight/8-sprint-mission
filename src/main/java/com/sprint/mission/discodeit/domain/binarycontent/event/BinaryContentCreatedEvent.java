package com.sprint.mission.discodeit.domain.binarycontent.event;

import java.util.UUID;

public record BinaryContentCreatedEvent
(
        UUID binaryContentId,
        byte[] bytes
) {}
