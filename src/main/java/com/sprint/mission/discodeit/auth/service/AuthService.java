package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.auth.dto.request.LoginRequest;

public interface AuthService
{
    UserDto login(LoginRequest loginRequest);
}
