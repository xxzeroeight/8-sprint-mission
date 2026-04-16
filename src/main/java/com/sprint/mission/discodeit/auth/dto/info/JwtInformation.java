package com.sprint.mission.discodeit.auth.dto.info;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

public record JwtInformation
(
        UserDto userDto,
        String accessToken,
        String refreshToken
)
{
    public JwtInformation rotate(String accessToken, String refreshToken) {
        return new JwtInformation(this.userDto, accessToken, refreshToken);
    }
}
