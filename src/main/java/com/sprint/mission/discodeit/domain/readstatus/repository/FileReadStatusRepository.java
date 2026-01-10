package com.sprint.mission.discodeit.domain.readstatus.repository;

import com.sprint.mission.discodeit.global.common.constants.FileConstants;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
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
public class FileReadStatusRepository implements ReadStatusRepository
{
    private final Path directory = Paths.get(FileConstants.READSTATUS_REPOSITORY_DATA_DIR);

    private FileReadStatusRepository() {
        FileUtil.init(directory);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path filePath = directory.resolve(readStatus.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, readStatus);

        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        ReadStatus readStatus = FileUtil.read(filePath);

        return Optional.ofNullable(readStatus);
    }

    @Override
    public List<ReadStatus> findAll() {
        return FileUtil.readAll(directory);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
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
    public void deleteAllByUserId(UUID userId) {
        findAllByUserId(userId)
                .forEach(readStatus -> deleteById(readStatus.getId()));
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        findAllByChannelId(channelId)
                .forEach(readStatus -> deleteById(readStatus.getId()));
    }
}
