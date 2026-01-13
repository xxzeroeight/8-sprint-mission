package com.sprint.mission.discodeit.domain.channel.domain;

import com.sprint.mission.discodeit.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import lombok.Getter;

@Getter
public class Channel extends BaseUpdatableEntity
{
    private String channelName;
    private String description;
    private ChannelType channelType;

    public Channel(String channelName, String description, ChannelType channelType) {
        this.channelName = channelName;
        this.description = description;
        this.channelType = channelType;
    }

    public void update(String channelName, String description) {
        if (channelName != null) {
            this.channelName = channelName;
        }
        if (description != null) {
            this.description = description;
        }
    }
}
