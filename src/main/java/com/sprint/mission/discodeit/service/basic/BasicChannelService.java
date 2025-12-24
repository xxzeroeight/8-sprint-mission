package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.ChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService
{
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel create(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = new Channel(
                publicChannelCreateRequest.channelName(),
                publicChannelCreateRequest.description(),
                ChannelType.PUBLIC
        );

        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        Channel createdChannel = channelRepository.save(channel);

        List<UUID> userIds = privateChannelCreateRequest.userIds();
        for (UUID userId : userIds) {
            ReadStatus readStatus = new ReadStatus(userId, createdChannel.getId(), Instant.MIN);
            readStatusRepository.save(readStatus);
        }

        return createdChannel;
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
    public Channel update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest) {
       Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

       if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
           throw ChannelUpdateNotAllowedException.forPrivateChannel("비밀 채널은 변경할 수 없습니다.");
       }

       channel.update(publicChannelUpdateRequest.updateName(), publicChannelUpdateRequest.updateDescription());

       return channelRepository.save(channel);
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
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(null);

        List<UUID> userIds = new ArrayList<>();
        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channel.getId());

            for (ReadStatus readStatus : readStatuses) {
                userIds.add(readStatus.getUserId());
            }
        }

        return new ChannelDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getChannelType(),
                userIds,
                lastMessageAt
        );
    }
}
