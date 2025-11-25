package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService
{
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        if (channelId == null) {
            return null;
        }

        if (authorId == null) {
            return null;
        }

        return messageRepository.save(new Message(authorId, channelId, content));
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public List<Message> findAllByAuthorId(UUID authorId) {
        return List.of();
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, String content) {
        if (id == null) {
            return null;
        }

        Message message = messageRepository.findById(id);

        if (message == null) {
            return null;
        }

        message.update(content);
        return message;
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            return;
        }

        messageRepository.delete(id);
    }
}
