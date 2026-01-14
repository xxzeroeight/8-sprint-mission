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
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = new Channel(
                publicChannelCreateRequest.name(),
                publicChannelCreateRequest.description(),
                ChannelType.PUBLIC
        );

        Channel savedChannel = channelRepository.save(channel);

        return channelMapper.toDto(savedChannel, List.of(), null);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        Channel createdChannel = channelRepository.save(channel);

        List<UUID> userIds = privateChannelCreateRequest.participantIds();
        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            ReadStatus readStatus = new ReadStatus(user, createdChannel, channel.getCreatedAt());
            readStatusRepository.save(readStatus);
        }

        return channelMapper.toDto(createdChannel, users, null);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channel -> toDto(channel))
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> channelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus ->  readStatus.getChannel().getId())
                .toList();

        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC) || channelIds.contains(channel.getId()))
                .map(channel -> toDto(channel))
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
       Channel savedChannel = channelRepository.save(channel);

       return toDto(savedChannel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

        channelRepository.deleteById(channelId);
        messageRepository.deleteAllByChannelId(channel.getId());
        readStatusRepository.deleteAllByChannelId(channel.getId());
    }

    private ChannelDto toDto(Channel channel) {
        List<User> users = getUsers(channel);
        Instant lassMessageAt = getLastMessageAt(channel.getId()).orElse(null);

        return channelMapper.toDto(channel, users, lassMessageAt);
    }

    private Optional<Instant> getLastMessageAt(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst();
    }

    private List<User> getUsers(Channel channel) {
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            List<UUID> userIds = readStatusRepository.findAllByChannelId(channel.getId()).stream()
                    .map(readStatus -> readStatus.getUser().getId())
                    .toList();

            return userRepository.findAllById(userIds);
        }

        return List.of();
    }
}
