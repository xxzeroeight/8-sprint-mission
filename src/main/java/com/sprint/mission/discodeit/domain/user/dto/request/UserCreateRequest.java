package com.sprint.mission.discodeit.domain.user.dto.request;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {}
