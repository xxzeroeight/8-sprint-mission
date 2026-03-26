package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

public interface AuthService
{
    UserDto getCurrentUseInfo(DiscodeitUserDetails discodeitUserDetails);
}
