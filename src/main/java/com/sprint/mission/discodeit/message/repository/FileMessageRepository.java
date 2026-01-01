package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.common.constants.FileConstants;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.common.util.FileUtil;
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
public class FileMessageRepository implements MessageRepository
{
    private final Path directory = Paths.get(FileConstants.MESSAGE_REPOSITORY_DATA_DIR);

    private FileMessageRepository() {
        FileUtil.init(directory);
    }

    @Override
    public Message save(Message message) {
        Path filePath = directory.resolve(message.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, message);

        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        Message message = FileUtil.read(filePath);

        return Optional.ofNullable(message);
    }

    @Override
    public List<Message> findAll() {
        return FileUtil.readAll(directory);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        findAllByChannelId(channelId)
                .forEach(message -> deleteById(message.getId()));
    }
}
