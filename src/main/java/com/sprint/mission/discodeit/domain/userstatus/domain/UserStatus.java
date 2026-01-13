package com.sprint.mission.discodeit.domain.userstatus.domain;

import com.sprint.mission.discodeit.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.user.domain.User;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class UserStatus extends BaseUpdatableEntity
{
    private static final int ONLINE_TIMEOUT = 5;

    private Instant lastActiveAt;

    private User user;

    // User가 있어야 존재가능.
    public UserStatus(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
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

    // 양방향 1:1 (무한루프 방지)
    public void setUser(User user) {
        this.user = user;

        if (user != null && user.getUserStatus() != this) {
            user.setUserStatus(this);
        }
    }
}
