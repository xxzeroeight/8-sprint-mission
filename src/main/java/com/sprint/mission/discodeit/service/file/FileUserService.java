package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService
{
    private static FileUserService instance;
    private final Path directory = Paths.get("data/users");

    private FileUserService() {
        FileUtil.init(directory);
    }

    public static FileUserService getInstance() {
        if (instance == null) {
            instance = new FileUserService();
        }

        return instance;
    }

    @Override
    public User create(String username, String password, String email) {
        User user = new User(username, password, email);
        Path filePath = directory.resolve(user.getId() + ".ser");
        FileUtil.save(filePath, user);

        return user;
    }

    @Override
    public User findById(UUID id) {
        if (id == null) {
            return null;
        }

        List<User> users = FileUtil.load(directory);

        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return FileUtil.load(directory);
    }

    @Override
    public User update(UUID id, String username, String password, String email) {
        if (id == null) {
            return null;
        }

        List<User> users = FileUtil.load(directory);
        User oldUser = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (oldUser == null) {
            return null;
        }

        oldUser.update(username, password, email);

        Path filePath = directory.resolve(oldUser.getId() + ".ser");
        FileUtil.save(filePath, oldUser);

        return oldUser;
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
