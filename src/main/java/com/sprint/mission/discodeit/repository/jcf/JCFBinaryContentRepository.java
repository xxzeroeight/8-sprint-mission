package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository
{
    private final Map<UUID, BinaryContent> data = new HashMap<>();

    public JCFBinaryContentRepository() {}

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);

        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        BinaryContent binaryContent = data.get(id);

        return Optional.ofNullable(binaryContent);
    }

    @Override
    public List<BinaryContent> findAll() {
        List<BinaryContent> binaryContents = data.values().stream().toList();

        return binaryContents;
    }

    @Override
    public List<BinaryContent> findAllByIds(List<UUID> ids) {
        return findAll().stream()
                .filter(binaryContent -> ids.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
