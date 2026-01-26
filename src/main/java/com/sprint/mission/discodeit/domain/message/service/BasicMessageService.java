package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.domain.message.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService
{
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;

    @Transactional
    @Override
    public MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> ChannelNotFoundException.byId(messageCreateRequest.channelId()));

        User author = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> UserNotFoundException.byId(messageCreateRequest.authorId()));

        List<BinaryContent> savedBinaryContents = binaryContentCreateRequests.stream()
                .map(this::createBinaryContent)
                .toList();

        Message message = new Message(channel, author, messageCreateRequest.content(), savedBinaryContents);

        Message savedMessage = messageRepository.save(message);

        return messageMapper.toDto(savedMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageDto findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .map(message -> messageMapper.toDto(message))
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageDto> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Instant cursor, Pageable pageable) {
        Pageable cursorPageable = PageRequest.of(0, pageable.getPageSize() + 1, Sort.by("createdAt").descending());

        Slice<Message> slice = cursor == null
                ? messageRepository.findFirstPageByChannelId(channelId, cursorPageable)
                : messageRepository.findNextPageByChannelId(channelId, cursor, cursorPageable);

        List<MessageDto> dtos = slice
                .map(messageMapper::toDto)
                .toList();

        boolean hasNext = dtos.size() > pageable.getPageSize();
        List<MessageDto> content = hasNext ? dtos.subList(0, pageable.getPageSize()) : dtos;

        Instant nextCursor = hasNext ? content.get(content.size() - 1).createdAt() : null;

        return pageResponseMapper.fromSlice(content, nextCursor, hasNext);
    }

    @Transactional
    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));

        message.update(messageUpdateRequest.updateContent());

        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> MessageNotFoundException.byId(messageId));

        binaryContentRepository.deleteAll(message.getAttachments());

        messageRepository.delete(message);
    }

    private BinaryContent createBinaryContent(BinaryContentCreateRequest binaryContentCreateRequest) {
        BinaryContent binaryContent = new BinaryContent(
                binaryContentCreateRequest.fileName(),
                (long) binaryContentCreateRequest.bytes().length,
                binaryContentCreateRequest.contentType()
        );

        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
        binaryContentStorage.save(savedBinaryContent.getId(), binaryContentCreateRequest.bytes());

        return savedBinaryContent;
    }
}
