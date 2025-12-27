package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.message.domain.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository
{
    Message save(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findAll();
    List<Message> findAllByChannelId(UUID channelId);
    void deleteById(UUID id);
    void deleteAllByChannelId(UUID channelId);
}
