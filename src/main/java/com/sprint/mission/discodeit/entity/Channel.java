package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.enums.ChannelType;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String channelName;
    private String description;
    private ChannelType channelType;

    private Long createdAt;
    private Long updatedAt;

    public Channel(String channelName, String description, ChannelType channelType) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.description = description;
        this.channelType = channelType;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String channelName, String description, ChannelType channelType) {
        if (channelName != null) { this.channelName = channelName; }
        if (description != null) { this.description = description; }
        if (channelType != null) { this.channelType = channelType; }

        this.updatedAt = System.currentTimeMillis();
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
