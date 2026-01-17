package com.sprint.mission.discodeit.domain.channel.repository;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
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
        JOIN FETCH c.readStatuses rs
        JOIN FETCH rs.user
        WHERE c.type = :channelType
        AND rs.user.id = :userId
        """)
    List<Channel> findAllPublicChannels(@Param("channelType") ChannelType type, @Param("userId") UUID userId);
}
