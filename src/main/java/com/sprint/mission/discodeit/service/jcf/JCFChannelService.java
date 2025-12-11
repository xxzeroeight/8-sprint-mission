package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService
{
    private static JCFChannelService instance;
    private final Map<UUID, Channel> data = new HashMap<>();

    private JCFChannelService() {}

    public static JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }

        return instance;
    }

    @Override
    public Channel create(String channelName, String description, ChannelType channelType) {
        Channel channel = new Channel(channelName, description, channelType);
        data.put(channel.getId(), channel);

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = data.get(id);

        return Optional.ofNullable(channel)
                .orElseThrow(() -> new ChannelException.ChannelNotFoundException(id));
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, String channelName, String description, ChannelType channelType) {
        Channel channelNullable = data.get(id);

        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new ChannelException.ChannelNotFoundException(id));

        channel.update(channelName, description, channelType);

        return channel;
    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new ChannelException.ChannelNotFoundException(id);
        }

        data.remove(id);
    }
}
