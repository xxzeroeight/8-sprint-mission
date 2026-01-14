package com.sprint.mission.discodeit.domain.channel.mapper;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChannelMapper
{
    @Mapping(target = "participants", source = "users")
    ChannelDto toDto(Channel channel, List<User> users, Instant lastMessageAt);
}
