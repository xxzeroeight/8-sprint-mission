package com.sprint.mission.discodeit.domain.userstatus.mapper;

import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.dto.domain.UserStatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStatusMapper
{
    UserStatusDto toDto(UserStatus userStatus);
}
