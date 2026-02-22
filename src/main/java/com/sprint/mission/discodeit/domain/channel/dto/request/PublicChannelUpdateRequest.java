package com.sprint.mission.discodeit.domain.channel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest
(
        @NotBlank(message = "채널명은 필수입니다.")
        @Size(min = 3, max = 30, message = "채널명은 3-30자 사이여야 합니다.")
        String updateName,

        @Size(max = 255, message = "설명은 최대 255자까지 입니다.")
        String updateDescription
) {}
