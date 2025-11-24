package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService
{
    Channel create(String channelName, String description, ChannelType channelType);

    Channel findById(UUID id);
    List<Channel> findAllChannels();

    Channel update(UUID id, String channelName, String description, ChannelType channelType);

    void delete(UUID id);
}
