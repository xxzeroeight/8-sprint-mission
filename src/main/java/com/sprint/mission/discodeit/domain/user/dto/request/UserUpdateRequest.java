package com.sprint.mission.discodeit.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest
(
        @Size(min = 2, max = 20, message = "사용자명은 2-20자 사이여야 합니다.")
        String newUsername,

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String newEmail,

        @Pattern(regexp = "^$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상, 영문과 숫자를 포함해야 합니다.")
        String newPassword
) {}
