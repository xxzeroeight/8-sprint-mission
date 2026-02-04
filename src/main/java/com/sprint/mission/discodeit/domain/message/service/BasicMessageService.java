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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
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
        log.debug("메시지 생성 처리 시작: authorId={}, channelId={}, content={}, count={}", messageCreateRequest.authorId(), messageCreateRequest.channelId(), messageCreateRequest.content(), binaryContentCreateRequests.size());

        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 채널(생성): channelId={}", messageCreateRequest.channelId());
                    return new ChannelNotFoundException(messageCreateRequest.channelId());
                });

        User author = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자(생성): authorId={}", messageCreateRequest.authorId());
                    return new UserNotFoundException(messageCreateRequest.authorId());
                });

        List<BinaryContent> savedBinaryContents = binaryContentCreateRequests.stream()
                .map(this::createBinaryContent)
                .toList();

        Message message = new Message(channel, author, messageCreateRequest.content(), savedBinaryContents);
        Message savedMessage = messageRepository.save(message);

        log.debug("메시지 생성 처리 완료: messageId={}", savedMessage.getId());

        return messageMapper.toDto(savedMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageDto findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .map(message -> messageMapper.toDto(message))
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 메시지(단건 조회): messageId={}", messageId);
                    return new MessageNotFoundException(messageId);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageDto> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Instant cursor, Pageable pageable) {
        log.debug("메시지 조회(다건) 처리 시작: channelId={}, cursor={}, pageSize={}", channelId, cursor, pageable.getPageSize());

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

        log.debug("메시지 조회(다건) 처리 완료: count={}, hasNext={}, nextCursor={}", content.size(), hasNext, nextCursor);

        return pageResponseMapper.fromSlice(content, nextCursor, hasNext);
    }

    @Transactional
    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        log.debug("메시지 정보 수정 처리 시작: messageId={}, content={}", messageId, messageUpdateRequest.updateContent());

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 메시지(수정): messageId={}", messageId);
                    return new MessageNotFoundException(messageId);
                });

        message.update(messageUpdateRequest.updateContent());

        log.debug("메시지 정보 수정 처리 완료: messageId={}", message.getId());

        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void delete(UUID messageId) {
        log.debug("메시지 삭제 처리 시작: messageId={}", messageId);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 메시지(삭제): messageId={}", messageId);
                    return new MessageNotFoundException(messageId);
                });

        binaryContentRepository.deleteAll(message.getAttachments());

        log.debug("메시지 삭제 처리 완료: messageId={}", messageId);

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
