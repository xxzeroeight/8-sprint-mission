package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;
    private final UUID channelId;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = Instant.now();
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant lastReadAt) {
        if (lastReadAt != null && !lastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = lastReadAt;
        }
    }
}
