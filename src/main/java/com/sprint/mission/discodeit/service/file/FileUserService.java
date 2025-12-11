package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.constants.FileConstants;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService
{
    private static FileUserService instance;
    private final Path directory = Paths.get(FileConstants.USER_DATA_DIR);

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
        Path filePath = directory.resolve(user.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, user);

        return user;
    }

    @Override
    public User findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        User user = FileUtil.read(filePath);

        return Optional.ofNullable(user)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));
    }

    @Override
    public List<User> findAllUsers() {
        return FileUtil.readAll(directory);
    }

    @Override
    public User update(UUID id, String username, String password, String email) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        User oldUser = FileUtil.read(filePath);

        User user = Optional.ofNullable(oldUser)
                        .orElseThrow(() -> new UserException.UserNotFoundException(id));

        user.update(username, password, email);
        FileUtil.save(filePath, user);

        return user;
    }

    @Override
    public void delete(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        if (Files.notExists(filePath)) {
            throw new UserException.UserNotFoundException(id);
        }

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
