package com.sprint.mission.discodeit.domain.readstatus.domain;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.global.entity.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "read_statuses",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_read_statuses_user_channel",
                columnNames = {"user_id", "channel_id"}
        ))
@Entity
public class ReadStatus extends BaseUpdatableEntity
{
    @Column(name = "last_read_at")
    private Instant lastReadAt;

    @Column(name = "notification_enabled", nullable = false)
    private boolean notificationEnabled;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_statuses_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_statuses_channel"))
    private Channel channel;

    public ReadStatus(User user, Channel channel, Instant lastReadAt, boolean notificationEnabled) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
        this.notificationEnabled = notificationEnabled;
    }

    public void update(Instant lastReadAt, Boolean notificationEnabled) {
        if (lastReadAt != null && !lastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = lastReadAt;
        }

        if (notificationEnabled != null) {
            this.notificationEnabled = notificationEnabled;
        }
    }
}
