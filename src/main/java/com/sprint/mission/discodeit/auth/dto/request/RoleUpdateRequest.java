package com.sprint.mission.discodeit.auth.dto.request;

import com.sprint.mission.discodeit.domain.user.domain.Role;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoleUpdateRequest(
        UUID userId,

        @NotNull(message = "권한은 필수입니다.")
        Role newRole
) {}
