package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Primary
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository
{
    private final Map<UUID, ReadStatus> data = new HashMap<>();

    public JCFReadStatusRepository() {}

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);

        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        ReadStatus readStatus = data.get(id);

        return Optional.ofNullable(readStatus);
    }

    @Override
    public List<ReadStatus> findAll() {
        List<ReadStatus> readStatuses = data.values().stream().toList();

        return readStatuses;
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
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
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
