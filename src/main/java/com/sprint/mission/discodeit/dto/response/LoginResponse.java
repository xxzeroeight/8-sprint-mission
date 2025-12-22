package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.entity.UserDto;

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
