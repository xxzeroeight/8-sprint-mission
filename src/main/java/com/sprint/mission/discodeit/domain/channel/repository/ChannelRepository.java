package com.sprint.mission.discodeit.domain.channel.repository;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository
{
    Channel save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    boolean existsById(UUID id);
    void delete(UUID id);
}
