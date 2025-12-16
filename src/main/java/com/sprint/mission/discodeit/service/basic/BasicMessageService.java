package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.*;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        if (!channelRepository.existsById(messageCreateRequest.channelId())) {
            throw DuplicateChannelException.byId(messageCreateRequest.channelId());
        }

        if (!userRepository.existsById(messageCreateRequest.authorId())) {
            throw DuplicateUserException.byId(messageCreateRequest.authorId());
        }

        List<UUID> binaryContentIds = binaryContentCreateRequests.stream()
                .map(binaryContentCreateRequest -> {
                    BinaryContent binaryContent = new BinaryContent(
                            binaryContentCreateRequest.fileName(),
                            (long) binaryContentCreateRequest.bytes().length,
                            binaryContentCreateRequest.contentType(),
                            binaryContentCreateRequest.bytes()
                    );

                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    return createdBinaryContent.getId();
                })
                .toList();

        Message message = new Message(
                messageCreateRequest.channelId(),
                messageCreateRequest.authorId(),
                messageCreateRequest.content(),
                binaryContentIds
        );

        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .toList();
    }

    @Override
    public Message update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));

        message.update(messageUpdateRequest.updateContent());

        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));

        message.getAttachmentIds()
                .forEach(messageAttachmentId -> binaryContentRepository.deleteById(messageAttachmentId));

        messageRepository.deleteById(messageId);
    }
}
