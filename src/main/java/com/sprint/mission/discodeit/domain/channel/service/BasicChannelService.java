package com.sprint.mission.discodeit.domain.channel.service;

import com.sprint.mission.discodeit.domain.BaseEntity;
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
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
        Channel channel = new Channel(
                publicChannelCreateRequest.name(),
                publicChannelCreateRequest.description(),
                ChannelType.PUBLIC
        );

        channelRepository.save(channel);

        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);

        privateChannelCreateRequest.participantIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> UserNotFoundException.byId(userId)))
                .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
                .forEach(channel.getReadStatuses()::add);

        channelRepository.save(channel);

        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channelMapper::toDto)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAllPublicChannels(ChannelType.PUBLIC, userId);

        return channels.stream()
                .map(channelMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest) {
       Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

       if (channel.getType().equals(ChannelType.PRIVATE)) {
           throw ChannelUpdateNotAllowedException.forPrivateChannel("비밀 채널은 변경할 수 없습니다.");
       }

       channel.update(publicChannelUpdateRequest.updateName(), publicChannelUpdateRequest.updateDescription());

       return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

        channelRepository.delete(channel);
    }
}
