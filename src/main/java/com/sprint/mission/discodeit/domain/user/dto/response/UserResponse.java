package com.sprint.mission.discodeit.domain.user.dto.response;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        BinaryContentDto profile,
        String username,
        String email,
        Boolean online
) {
    public static UserResponse from(UserDto userDto) {
        return new UserResponse(
                userDto.id(),
                userDto.profile(),
                userDto.username(),
                userDto.email(),
                userDto.online()
        );
    }
}
