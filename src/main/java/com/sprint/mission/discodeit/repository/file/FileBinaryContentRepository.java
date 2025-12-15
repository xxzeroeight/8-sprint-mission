package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constants.FileConstants;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    public void deleteById(UUID id) {
        Path filePath = directory.resolve(id + FileConstants.FILE_EXTENSION);

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
