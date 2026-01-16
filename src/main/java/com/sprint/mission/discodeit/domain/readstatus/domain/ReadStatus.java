package com.sprint.mission.discodeit.domain.readstatus.domain;

import com.sprint.mission.discodeit.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.user.domain.User;
import jakarta.persistence.*;
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
    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_statuses_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_statuses_channel"))
    private Channel channel;

    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant lastReadAt) {
        if (lastReadAt != null && !lastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = lastReadAt;
        }
    }
}
