package com.sprint.mission.discodeit.domain.userstatus.mapper;

import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.dto.domain.UserStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper
{
    @Mapping(target = "userId", expression = "java(userStatus.getUser().getId())")
    UserStatusDto toDto(UserStatus userStatus);
}
