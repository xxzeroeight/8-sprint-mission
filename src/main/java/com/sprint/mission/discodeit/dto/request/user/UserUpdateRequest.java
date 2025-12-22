package com.sprint.mission.discodeit.dto.request.user;

public record UserUpdateRequest(
        String updateUsername,
        String updateEmail,
        String updatePassword
) {}
