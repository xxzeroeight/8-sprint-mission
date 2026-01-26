package com.sprint.mission.discodeit.domain.channel.repository;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID>
{
    @Query(
        """
        SELECT DISTINCT c
        FROM Channel c
        LEFT JOIN FETCH c.readStatuses rs
        LEFT JOIN FETCH rs.user
        WHERE c.type = 'PUBLIC'
           OR c.id IN (
               SELECT rs2.channel.id 
               FROM ReadStatus rs2 
               WHERE rs2.user.id = :userId)
        """)
    List<Channel> findAllAccessibleByUserId(@Param("userId") UUID userId);
}
