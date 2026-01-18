package com.sprint.mission.discodeit.domain.channel.domain;

import com.sprint.mission.discodeit.global.entity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channels")
@Entity
public class Channel extends BaseUpdatableEntity
{
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    // ORDINAL: enum이 수정되면 서수가 꼬임.
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChannelType type;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> messages =  new ArrayList<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReadStatus> readStatuses = new ArrayList<>();

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
