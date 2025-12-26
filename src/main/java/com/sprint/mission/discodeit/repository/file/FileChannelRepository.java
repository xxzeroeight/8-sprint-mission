package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constants.FileConstants;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
@Repository
public class FileChannelRepository implements ChannelRepository
{
    private final Path directory = Paths.get(FileConstants.CHANNEL_REPOSITORY_DATA_DIR);

    private FileChannelRepository() {
        FileUtil.init(directory);
    }

    @Override
    public Channel save(Channel channel) {
        Path filePath = directory.resolve(channel.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, channel);

        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        Channel channel = FileUtil.read(filePath);

        return Optional.ofNullable(channel);
    }

    @Override
    public List<Channel> findAll() {
        return FileUtil.readAll(directory);
    }

    @Override
    public boolean existsById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        return Files.exists(filePath);
    }

    @Override
    public void delete(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
