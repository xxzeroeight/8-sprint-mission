package com.sprint.mission.discodeit.domain.user.dto.domain;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.user.domain.User;

import java.util.UUID;

public record UserDto(
        UUID id,
        BinaryContentDto profile,
        String username,
        String email,
        Boolean online
) {
    public static UserDto from(User user, Boolean online) {
        return new UserDto(
                user.getId(),
                user.getProfile() != null ? BinaryContentDto.from(user.getProfile()) : null,
                user.getUsername(),
                user.getEmail(),
                online
        );
    }
}
