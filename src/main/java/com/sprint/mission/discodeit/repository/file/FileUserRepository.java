package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constants.FileConstants;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserRepository implements UserRepository
{
    private static FileUserRepository instance;
    private final Path directory = Paths.get(FileConstants.USER_REPOSITORY_DATA_DIR);

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
        Path filePath = directory.resolve(user.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, user);

        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        User user = FileUtil.read(filePath);

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        return FileUtil.readAll(directory);
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
