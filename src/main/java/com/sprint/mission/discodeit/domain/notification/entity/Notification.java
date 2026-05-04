package com.sprint.mission.discodeit.domain.notification.entity;

import com.sprint.mission.discodeit.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity
{
    @Column(name = "receiver_id", nullable = false)
    private UUID receiverId;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    public Notification(UUID receiverId, String title, String content) {
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
    }
}
