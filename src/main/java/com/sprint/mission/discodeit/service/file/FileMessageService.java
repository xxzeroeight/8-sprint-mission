package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageException;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService
{
    private static FileMessageService instance;
    private final Path directory = Paths.get("data/messages");

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
        Path filePath = directory.resolve(message.getId() + ".ser");
        FileUtil.save(filePath, message);

        return message;
    }

    @Override
    public Message findById(UUID id) {
        if (id == null) {
            return null;
        }

        List<Message> messages = FileUtil.load(directory);

        return messages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> findAllMessages() {
        return FileUtil.load(directory);
    }

    @Override
    public Message update(UUID id, String content) {
        List<Message> messages = FileUtil.load(directory);

        Message oldMessage = messages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new MessageException.MessageNotFoundException(id));

        oldMessage.update(content);

        Path filePath = directory.resolve(oldMessage.getId() + ".ser");
        FileUtil.save(filePath, oldMessage);

        return oldMessage;
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
