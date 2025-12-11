package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService
{
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(String channelName, String description, ChannelType channelType) {
        return channelRepository.save(new Channel(channelName, description, channelType));
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelException.ChannelNotFoundException(id));

        return channel;
    }

    @Override
    public List<Channel> findAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String channelName, String description, ChannelType channelType) {
        if (id == null) {
            return null;
        }

        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelException.ChannelNotFoundException(id));

        channel.update(channelName, description, channelType);

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            return;
        }

        channelRepository.delete(id);
    }
}
