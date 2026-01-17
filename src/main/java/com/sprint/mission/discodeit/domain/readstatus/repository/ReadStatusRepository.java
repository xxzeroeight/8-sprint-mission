package com.sprint.mission.discodeit.domain.readstatus.repository;

import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    @Query(
        """
        SELECT rs
        FROM ReadStatus rs
        JOIN FETCH rs.channel
        WHERE rs.user.id = :userId
        """)
    List<ReadStatus> findAllByUserId(@Param("userId") UUID userId);

    @Query(
        """
        SELECT rs
        FROM ReadStatus rs
        JOIN FETCH rs.user u
        LEFT JOIN FETCH u.profile
        LEFT JOIN FETCH u.userStatus
        WHERE rs.user.id = :userId
        """)
    List<ReadStatus> findAllByChannelId(@Param("channelId") UUID channelId);
}
