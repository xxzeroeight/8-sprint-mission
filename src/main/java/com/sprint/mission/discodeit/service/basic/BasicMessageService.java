package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService
{
    private final MessageRepository messageRepository;

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
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageException.MessageNotFoundException(id));

        return message;
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

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageException.MessageNotFoundException(id));

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
