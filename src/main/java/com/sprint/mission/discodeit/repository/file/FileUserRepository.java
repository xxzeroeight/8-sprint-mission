package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constants.FileConstants;
import com.sprint.mission.discodeit.entity.User;
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
    private final Path directory = Paths.get(FileConstants.USER_REPOSITORY_DATA_DIR);

    private FileUserRepository() {
        FileUtil.init(directory);
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
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public List<User> findAll() {
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
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
