package com.sprint.mission.discodeit.dto.request;

public record UserUpdateRequest(
        String updateUsername,
        String updateEmail,
        String updatePassword
) {}
