package com.sprint.mission.discodeit.domain.readstatus.domain;

import com.sprint.mission.discodeit.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.user.domain.User;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ReadStatus extends BaseUpdatableEntity
{
    private Instant lastReadAt;

    private final User user;
    private final Channel channel;

    // User, Channel이 있어야 존재가능.
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
