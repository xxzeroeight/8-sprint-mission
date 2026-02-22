package com.sprint.mission.discodeit.domain.message.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageUpdateRequest
(
        @NotBlank(message = "메시지 내용은 필수입니다.")
        @Size(max = 1000, message = "메시지 내용은 최대 1000자까지 입니다.")
        String updateContent
) {}
