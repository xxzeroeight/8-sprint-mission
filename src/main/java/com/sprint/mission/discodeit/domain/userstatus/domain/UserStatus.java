package com.sprint.mission.discodeit.domain.userstatus.domain;

import com.sprint.mission.discodeit.global.entity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_statuses")
@Entity
public class UserStatus extends BaseUpdatableEntity
{
    private static final int ONLINE_TIMEOUT = 5;

    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_user_statuses_user"))
    private User user;

    public UserStatus(User user, Instant lastActiveAt) {
        this.user = user;
        this.lastActiveAt = lastActiveAt;
    }

    public void update(Instant lastActiveAt) {
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
        }
    }

    public Boolean isOnline() {
        Instant instant = Instant.now().minus(Duration.ofMinutes(ONLINE_TIMEOUT));

        return lastActiveAt.isAfter(instant);
    }
}
