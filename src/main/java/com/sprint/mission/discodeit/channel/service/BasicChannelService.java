package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.channel.exception.ChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = new Channel(
                publicChannelCreateRequest.channelName(),
                publicChannelCreateRequest.description(),
                ChannelType.PUBLIC
        );

        Channel savedChannel = channelRepository.save(channel);

        return ChannelDto.from(savedChannel, List.of(), null);
    }

    @Override
    public ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        Channel createdChannel = channelRepository.save(channel);

        List<UUID> userIds = privateChannelCreateRequest.userIds();
        for (UUID userId : userIds) {
            ReadStatus readStatus = new ReadStatus(userId, createdChannel.getId(), Instant.MIN);
            readStatusRepository.save(readStatus);
        }

        return ChannelDto.from(createdChannel, userIds, null);
    }

    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channel -> toDto(channel))
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> channelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        return channelRepository.findAll().stream()
                .filter(channel -> channel.getChannelType().equals(ChannelType.PUBLIC) || channelIds.contains(channel.getId()))
                .map(channel -> toDto(channel))
                .toList();
    }

    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest) {
       Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

       if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
           throw ChannelUpdateNotAllowedException.forPrivateChannel("비밀 채널은 변경할 수 없습니다.");
       }

       channel.update(publicChannelUpdateRequest.updateName(), publicChannelUpdateRequest.updateDescription());
       Channel savedChannel = channelRepository.save(channel);

       return toDto(savedChannel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

        channelRepository.delete(channelId);
        messageRepository.deleteAllByChannelId(channel.getId());
        readStatusRepository.deleteAllByChannelId(channel.getId());
    }

    private ChannelDto toDto(Channel channel) {
        List<UUID> userIds = getUserIds(channel);
        Instant lassMessageAt = getLastMessageAt(channel.getId()).orElse(null);

        return ChannelDto.from(channel, userIds, lassMessageAt);
    }

    private Optional<Instant> getLastMessageAt(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst();
    }

    private List<UUID> getUserIds(Channel channel) {
        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            return readStatusRepository.findAllByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .toList();
        }

        return List.of();
    }
}
