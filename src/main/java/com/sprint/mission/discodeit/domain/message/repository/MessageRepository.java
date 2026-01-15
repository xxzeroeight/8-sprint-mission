package com.sprint.mission.discodeit.domain.message.repository;

import com.sprint.mission.discodeit.domain.message.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>
{
    @Query(
        """
        SELECT MAX(m.createdAt)
        FROM Message m
        WHERE m.channel.id = :channelId
        """
    )
    Optional<Instant> findLastMessageAtByChannelId(UUID channelId);

    @EntityGraph(attributePaths = {"author.profile", "author.userStatus"})
    @Query(
        """
        SELECT m
        FROM Message m
        WHERE m.channel.id = :channelId
        AND (:cursor IS NULL OR m.createdAt < :cursor)
        ORDER BY m.createdAt DESC
        """)
    Slice<Message> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Instant cursor, Pageable pageable);

    void deleteAllByChannelId(UUID channelId);
}
