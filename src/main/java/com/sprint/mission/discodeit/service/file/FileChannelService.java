package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.constants.FileConstants;
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
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService
{
    private static FileChannelService instance;
    private final Path directory = Paths.get(FileConstants.CHANNEL_DATA_DIR);

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
        Path filePath = directory.resolve(channel.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, channel);

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        Channel channel = FileUtil.read(filePath);

        return Optional.ofNullable(channel)
                .orElseThrow(() -> new ChannelException.ChannelNotFoundException(id));
    }

    @Override
    public List<Channel> findAllChannels() {
        return FileUtil.readAll(directory);
    }

    @Override
    public Channel update(UUID id, String channelName, String description, ChannelType channelType) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        Channel oldChannel = FileUtil.read(filePath);

        Channel channel = Optional.ofNullable(oldChannel)
                .orElseThrow(() -> new ChannelException.ChannelNotFoundException(id));

        channel.update(channelName, description, channelType);
        FileUtil.save(filePath, channel);

        return channel;
    }

    @Override
    public void delete(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        if (Files.notExists(filePath)) {
            throw new ChannelException.ChannelNotFoundException(id);
        }

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
