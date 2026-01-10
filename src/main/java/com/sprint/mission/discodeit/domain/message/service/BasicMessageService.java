package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
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
    public MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        if (!channelRepository.existsById(messageCreateRequest.channelId())) {
            throw ChannelNotFoundException.byId(messageCreateRequest.channelId());
        }

        if (!userRepository.existsById(messageCreateRequest.authorId())) {
            throw UserNotFoundException.byId(messageCreateRequest.authorId());
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

        Message savedMessage = messageRepository.save(message);

        return MessageDto.from(savedMessage);
    }

    @Override
    public MessageDto findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .map(message -> MessageDto.from(message))
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(message -> MessageDto.from(message))
                .toList();
    }

    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));

        message.update(messageUpdateRequest.updateContent());
        Message savedMessage = messageRepository.save(message);

        return MessageDto.from(savedMessage);
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
