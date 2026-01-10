package com.sprint.mission.discodeit.domain.channel.domain;

import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final UUID id;

    private String channelName;
    private String description;
    private ChannelType channelType;

    private final Instant createdAt;
    private Instant updatedAt;

    public Channel(String channelName, String description, ChannelType channelType) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.description = description;
        this.channelType = channelType;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public void update(String channelName, String description) {
        if (channelName != null) {
            this.channelName = channelName;
        }
        if (description != null) {
            this.description = description;
        }

        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", channelType=" + channelType +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
