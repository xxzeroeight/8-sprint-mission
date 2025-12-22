package com.sprint.mission.discodeit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sprint.mission.discodeit.dto.entity.UserDto;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        UUID profileId,
        String username,
        String email,
        boolean online,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant updatedAt
) {
    public static UserResponse from(UserDto user) {
        return new UserResponse(
                user.id(),
                user.profileId(),
                user.username(),
                user.email(),
                user.online(),
                user.createdAt(),
                user.updatedAt()
        );
    }
}
