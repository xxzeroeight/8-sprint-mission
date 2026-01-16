package com.sprint.mission.discodeit.domain.readstatus.repository;

import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID>
{
    @Query("SELECT r FROM ReadStatus r JOIN FETCH r.user")
    List<ReadStatus> findAllByUserId(UUID userId);

    @Query("SELECT r FROM ReadStatus r JOIN FETCH r.channel")
    List<ReadStatus> findAllByChannelId(UUID channelId);
}
