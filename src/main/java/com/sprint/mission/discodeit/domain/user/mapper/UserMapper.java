package com.sprint.mission.discodeit.domain.user.mapper;

import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.global.secutiry.JwtRegistry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper
{
    protected JwtRegistry jwtRegistry;

    @Autowired
    public void setJwtRegistry(JwtRegistry jwtRegistry) {
        this.jwtRegistry = jwtRegistry;
    }

    @Mapping(target = "online", expression = "java(jwtRegistry.hasActiveJwtInformationByUserId(user.getId()))")
    public abstract UserDto toDto(User user);
}
