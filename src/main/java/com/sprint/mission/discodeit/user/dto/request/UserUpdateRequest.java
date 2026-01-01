package com.sprint.mission.discodeit.user.dto.request;

public record UserUpdateRequest(
        String updateUsername,
        String updateEmail,
        String updatePassword
) {}
