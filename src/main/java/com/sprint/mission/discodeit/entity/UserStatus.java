package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = Instant.now();
        this.lastActiveAt = lastActiveAt;
    }

    public void update(Instant lastActiveAt) {
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
        }
    }

    public Boolean isOnline() {
        Instant instant = Instant.now().minus(Duration.ofMinutes(5));

        return lastActiveAt.isAfter(instant);
    }
}
