package com.sprint.mission.discodeit.domain.message.repository;

import com.sprint.mission.discodeit.domain.message.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>
{
    @Query("SELECT MAX(m.createdAt) FROM Message m WHERE m.channel.id = :channelId")
    Optional<Instant> findLastMessageAtByChannelId(@Param("channelId") UUID channelId);

    @Query(
        """
        SELECT m FROM Message m
        LEFT JOIN FETCH m.author a
        LEFT JOIN FETCH a.profile
        LEFT JOIN FETCH a.userStatus
        WHERE m.channel.id = :channelId
        ORDER BY m.createdAt DESC
        """)
    Slice<Message> findFirstPageByChannelId(UUID channelId, Pageable pageable);

    @Query(
        """
        SELECT m FROM Message m
        LEFT JOIN FETCH m.author a
        LEFT JOIN FETCH a.profile
        LEFT JOIN FETCH a.userStatus
        WHERE m.channel.id = :channelId
        AND m.createdAt < :cursor
        ORDER BY m.createdAt DESC
        """)
    Slice<Message> findNextPageByChannelId(UUID channelId, Instant cursor, Pageable pageable);
}
