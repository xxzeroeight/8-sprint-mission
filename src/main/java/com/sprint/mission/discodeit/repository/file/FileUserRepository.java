package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository
{
    private final Path directory = Paths.get("rdata/users");

    private static FileUserRepository instance;

    private FileUserRepository() {
        FileUtil.init(directory);
    }

    public static FileUserRepository getInstance() {
        if (instance == null) {
            instance = new FileUserRepository();
        }

        return instance;
    }

    @Override
    public User save(User user) {
        Path filePath = directory.resolve(user.getId() + ".ser");
        FileUtil.save(filePath, user);

        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        List<User> users = FileUtil.load(directory);

        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
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
