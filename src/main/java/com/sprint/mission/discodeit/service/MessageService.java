package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService
{
    Message create(String content, UUID authorId, UUID channelId);

    Message findById(UUID id);
    List<Message> findAllByChannelId(UUID channelId);
    List<Message> findAllByAuthorId(UUID authorId);
    List<Message> findAllMessages();

    Message update(UUID id, String content);

    void delete(UUID id);
}
