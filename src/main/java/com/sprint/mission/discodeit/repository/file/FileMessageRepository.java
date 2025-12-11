package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository
{
    private final Path directory = Paths.get("rdata/messages");

    private static FileMessageRepository instance;

    private FileMessageRepository() {
        FileUtil.init(directory);
    }

    public static FileMessageRepository getInstance() {
        if (instance == null) {
            instance = new FileMessageRepository();
        }

        return instance;
    }

    @Override
    public Message save(Message message) {
        Path filePath = directory.resolve(message.getId() + ".ser");
        FileUtil.save(filePath, message);

        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        List<Message> messages = FileUtil.load(directory);

        return messages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Message> findAll() {
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
