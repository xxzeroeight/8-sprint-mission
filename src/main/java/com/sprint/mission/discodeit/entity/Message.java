package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID channelId;
    private final UUID authorId;
    private List<UUID> attachmentIds;

    private String content;

    private final Instant createdAt;
    private Instant updatedAt;

    public Message(UUID channelId, UUID authorId, String content, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public void update(String content) {
        if (content != null) {
            this.content = content;
        }

        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", authorId=" + authorId +
                ", attachmentIds=" + attachmentIds +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
