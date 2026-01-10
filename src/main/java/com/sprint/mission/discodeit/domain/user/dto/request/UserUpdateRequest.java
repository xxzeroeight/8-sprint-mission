package com.sprint.mission.discodeit.domain.user.dto.request;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {}
