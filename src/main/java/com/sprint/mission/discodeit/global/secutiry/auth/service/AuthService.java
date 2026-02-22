package com.sprint.mission.discodeit.global.secutiry.auth.service;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.global.secutiry.auth.dto.request.LoginRequest;

public interface AuthService
{
    UserDto login(LoginRequest loginRequest);
}
