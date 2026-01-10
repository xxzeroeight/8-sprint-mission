package com.sprint.mission.discodeit.domain.readstatus.repository;

import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository
{
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAll();
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    void deleteAllByUserId(UUID userId);
    void deleteAllByChannelId(UUID channelId);
}
