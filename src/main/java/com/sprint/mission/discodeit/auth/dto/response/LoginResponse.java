package com.sprint.mission.discodeit.auth.dto.response;

import com.sprint.mission.discodeit.user.dto.domain.UserDto;

public record LoginResponse(
        String username,
        String email
) {
    public static LoginResponse from(UserDto user) {
        return new LoginResponse(
                user.username(),
                user.email()
        );
    }
}
