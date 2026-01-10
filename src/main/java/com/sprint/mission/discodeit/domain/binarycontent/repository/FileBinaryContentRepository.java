package com.sprint.mission.discodeit.domain.binarycontent.repository;

import com.sprint.mission.discodeit.global.common.constants.FileConstants;
import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.global.common.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository
{
    private final Path directory = Paths.get(FileConstants.BINARYCONTENT_REPOSITORY_DATA_DIR);

    private FileBinaryContentRepository() {
        FileUtil.init(directory);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Path filePath = directory.resolve(binaryContent.getId() + FileConstants.FILE_EXTENSION);
        FileUtil.save(filePath, binaryContent);

        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);
        BinaryContent binaryContent = FileUtil.read(filePath);

        return Optional.ofNullable(binaryContent);
    }

    @Override
    public List<BinaryContent> findAll() {
        return FileUtil.readAll(directory);
    }

    @Override
    public List<BinaryContent> findAllByIds(List<UUID> ids) {
        Set<UUID> idSet = new HashSet<>(ids);

        return findAll().stream()
                .filter(binaryContent -> idSet.contains(binaryContent.getId()))
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
}
