package com.sprint.mission.discodeit.domain.userstatus.repository;

import com.sprint.mission.discodeit.global.common.constants.FileConstants;
import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import com.sprint.mission.discodeit.global.common.util.FileUtil;
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
public class FileUserStatusRepository implements UserStatusRepository
{
    private final Path directory = Paths.get(FileConstants.USERSTATUS_REPOSITORY_DATA_DIR);

    private FileUserStatusRepository() {
        FileUtil.init(directory);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path filePath = directory.resolve(userStatus.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, userStatus);

        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        UserStatus userStatus = FileUtil.read(filePath);

        return Optional.ofNullable(userStatus);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return FileUtil.readAll(directory);
    }

    @Override
    public boolean existsById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        return Files.exists(filePath);
    }

    @Override
    public void deleteById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findByUserId(userId)
                .ifPresent(userStatus -> deleteById(userStatus.getId()));
    }
}
