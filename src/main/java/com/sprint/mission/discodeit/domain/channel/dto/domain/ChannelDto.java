package com.sprint.mission.discodeit.domain.channel.dto.domain;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String name,
        String description,
        ChannelType channelType,
        List<UserDto> participants,
        Instant lastMessageAt
) {
    public static ChannelDto from(Channel channel, List<User> users, Instant lastMessageAt) {
        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                users.stream()
                        .map(user -> UserDto.from(user, user.getUserStatus().isOnline()))
                        .toList(),
                lastMessageAt
        );
    }
}
