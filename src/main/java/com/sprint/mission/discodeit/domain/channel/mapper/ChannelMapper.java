package com.sprint.mission.discodeit.domain.channel.mapper;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChannelMapper
{
    @Mapping(target = "participants", expression = "java(getParticipants(channel))")
    @Mapping(target = "lastMessageAt", expression = "java(getLastMessageAt(channel))")
    ChannelDto toDto(Channel channel);

    @Mapping(target = "online", expression = "java(user.getUserStatus() != null && user.getUserStatus().isOnline())")
    UserDto mapUser(User user);

    default List<UserDto> getParticipants(Channel channel) {
        if (channel.getType() == ChannelType.PUBLIC) {
            return Collections.emptyList();
        }

        return channel.getReadStatuses().stream()
                .map(ReadStatus::getUser)
                .map(this::mapUser)
                .toList();
    }

    default Instant getLastMessageAt(Channel channel) {
        return channel.getMessages().stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
    }
}
