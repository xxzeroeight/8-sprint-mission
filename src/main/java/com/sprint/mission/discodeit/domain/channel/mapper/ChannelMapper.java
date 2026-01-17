package com.sprint.mission.discodeit.domain.channel.mapper;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelMapper
{
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper userMapper;

    public ChannelDto toDto(Channel channel) {
        if (channel == null) return null;

        Instant lastMessageAt = messageRepository.findLastMessageAtByChannelId(channel.getId()).orElse(null);

        List<UserDto> participants = readStatusRepository.findAllByChannelId(channel.getId()).stream()
                .map(ReadStatus::getUser)
                .map(userMapper::toDto)
                .toList();

        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                participants,
                lastMessageAt
        );
    }
}
