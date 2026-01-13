package com.sprint.mission.discodeit.domain.channel.domain;

import com.sprint.mission.discodeit.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channels")
@Entity
public class Channel extends BaseUpdatableEntity
{
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    // ORDINAL: enum이 수정되면 서수가 꼬임.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    public Channel(String name, String description, ChannelType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public void update(String name, String description) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
    }
}
