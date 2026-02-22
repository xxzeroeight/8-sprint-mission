package com.sprint.mission.discodeit.global.secutiry.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest
(
        @NotBlank(message = "사용자명은 필수입니다.")
        @Size(min = 2, max = 20, message = "사용자명은 2-20자여야 합니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {}
