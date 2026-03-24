package com.sprint.mission.discodeit.auth.dto.response;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String username,
        String email
) {
    public static LoginResponse from(UserDto user) {
        return new LoginResponse(
                user.id(),
                user.username(),
                user.email()
        );
    }
}
