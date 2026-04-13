package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.info.JwtInformation;

public interface AuthService
{
    JwtInformation refreshToken(String refreshToken);
}
