package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private UUID profileId;

    private String username;
    private transient String password;
    private String email;

    private final Instant createdAt;
    private Instant updatedAt;

    public User(String username, String password, String email, UUID profileId) {
        this.id = UUID.randomUUID();
        this.profileId = profileId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public void update(String username, String password, String email, UUID profileId) {
        if (username != null) this.username = username;
        if (email != null) this.email = email;
        if (profileId != null) this.profileId = profileId;

        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", profileId=" + profileId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
