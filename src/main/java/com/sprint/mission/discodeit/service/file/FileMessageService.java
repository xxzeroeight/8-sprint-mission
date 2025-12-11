package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.constants.FileConstants;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageException;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageService implements MessageService
{
    private static FileMessageService instance;
    private final Path directory = Paths.get(FileConstants.MESSAGE_DATA_DIR);

    private FileMessageService() {
        FileUtil.init(directory);
    }

    public static FileMessageService getInstance() {
        if (instance == null) {
            instance = new FileMessageService();
        }

        return instance;
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        Message message = new Message(channelId, authorId, content);
        Path filePath = directory.resolve(message.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, message);

        return message;
    }

    @Override
    public Message findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        Message message = FileUtil.read(filePath);

        return Optional.ofNullable(message)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));
    }

    @Override
    public List<Message> findAllMessages() {
        return FileUtil.readAll(directory);
    }

    @Override
    public Message update(UUID id, String content) {
        Path filPath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        Message oldMessage = FileUtil.read(filPath);

        Message message = Optional.ofNullable(oldMessage)
                .orElseThrow(() -> new MessageException.MessageNotFoundException(id));

        message.update(content);
        FileUtil.save(filPath, message);

        return message;
    }

    @Override
    public void delete(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        if (Files.notExists(filePath)) {
            throw new MessageException.MessageNotFoundException(id);
        }

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
