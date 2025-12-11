package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService
{
    private static FileChannelService instance;
    private final Path directory = Paths.get("data/channels");

    private FileChannelService() {
        FileUtil.init(directory);
    }

    public static FileChannelService getInstance() {
        if (instance == null) {
            instance = new FileChannelService();
        }

        return instance;
    }

    @Override
    public Channel create(String channelName, String description, ChannelType channelType) {
        Channel channel = new Channel(channelName, description, channelType);
        Path filePath = directory.resolve(channel.getId() + ".ser");
        FileUtil.save(filePath, channel);

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        List<Channel> channels = FileUtil.load(directory);

        return channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ChannelException.ChannelNotFoundException(id));
    }

    @Override
    public List<Channel> findAllChannels() {
        return FileUtil.load(directory);
    }

    @Override
    public Channel update(UUID id, String channelName, String description, ChannelType channelType) {
        List<Channel> channels = FileUtil.load(directory);

        Channel oldChannel = channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ChannelException.ChannelNotFoundException(id));

        oldChannel.update(channelName, description, channelType);

        Path filePath = directory.resolve(oldChannel.getId() + ".ser");
        FileUtil.save(filePath, oldChannel);

        return oldChannel;
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            return;
        }

        Path filePath = directory.resolve(id + ".ser");

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
