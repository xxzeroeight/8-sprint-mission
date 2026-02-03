package com.sprint.mission.discodeit.domain.channel.service;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.domain.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.domain.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService
{
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest) {
        log.debug("채널 생성 처리 시작(공개): name={}, description={}", publicChannelCreateRequest.name(), publicChannelCreateRequest.description());

        Channel channel = new Channel(
                publicChannelCreateRequest.name(),
                publicChannelCreateRequest.description(),
                ChannelType.PUBLIC
        );

        channelRepository.save(channel);

        log.debug("채널 생성 처리 완료(공개): channelId={}", channel.getId());

        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        log.debug("채널 생성 처리 시작(비공개): paricipants={}", privateChannelCreateRequest.participantIds().size());

        Channel channel = new Channel(null, null, ChannelType.PRIVATE);

        privateChannelCreateRequest.participantIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> {
                            log.warn("존재하지 않는 사용자: userId={}", userId);
                            return UserNotFoundException.byId(userId);
                        }))
                .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
                .forEach(channel.getReadStatuses()::add);

        channelRepository.save(channel);

        log.debug("채널 생성 처리 완료(비공개): channelId={}", channel.getId());

        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channelMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 채널(단건 조회): channelId={}", channelId);
                    return ChannelNotFoundException.byId(channelId);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelRepository.findAllAccessibleByUserId(userId).stream()
                .map(channelMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest) {
        log.debug("채널 수정 처리 시작: channelId={}, name={}, description={}", channelId, publicChannelUpdateRequest.updateName(), publicChannelUpdateRequest.updateDescription());

       Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("존재 하지 않는 채널(수정): channelId={}", channelId);
                    return ChannelNotFoundException.byId(channelId);
                });

       if (channel.getType().equals(ChannelType.PRIVATE)) {
           log.warn("비공개 채널 수정 시도: channnelId={}", channelId);
           throw ChannelUpdateNotAllowedException.forPrivateChannel("비밀 채널은 변경할 수 없습니다.");
       }

       channel.update(publicChannelUpdateRequest.updateName(), publicChannelUpdateRequest.updateDescription());

       log.debug("채널 수정 처리 완료: channelId={}", channel.getId());

       return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.debug("채널 삭제 처리 시작:  channelId={}", channelId);

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("존재 하지 않는 채널(삭제): channelId={}", channelId);
                    return ChannelNotFoundException.byId(channelId);
                });

        channelRepository.delete(channel);

        log.debug("채널 삭제 처리 완료: channelId={}", channel.getId());
    }
}
