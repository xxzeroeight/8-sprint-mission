package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository
{
    private final Path directory = Paths.get("rdata/channels");

    private static FileChannelRepository instance;

    private FileChannelRepository() {
        FileUtil.init(directory);
    }

    public static FileChannelRepository getInstance() {
        if (instance == null) {
            instance = new FileChannelRepository();
        }

        return instance;
    }

    @Override
    public Channel save(Channel channel) {
        Path filePath = directory.resolve(channel.getId() + ".ser");
        FileUtil.save(filePath, channel);

        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        List<Channel> channels = FileUtil.load(directory);

        return channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> findAll() {
        return FileUtil.load(directory);
    }

    @Override
    public void delete(UUID id) {
        Path filePath = directory.resolve(id + ".ser");

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
