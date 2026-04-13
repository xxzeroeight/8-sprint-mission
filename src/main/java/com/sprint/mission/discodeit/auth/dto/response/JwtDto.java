package com.sprint.mission.discodeit.auth.dto.response;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

public record JwtDto
(
        UserDto userDto,
        String accessToken
) {}
