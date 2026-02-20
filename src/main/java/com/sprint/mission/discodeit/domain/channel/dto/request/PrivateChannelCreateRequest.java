package com.sprint.mission.discodeit.domain.channel.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest
(
        @NotEmpty(message = "참가자 목록은 필수입니다.")
        @Size(min = 2, max = 2, message = "정확히 2명의 참가자가 필요합니다.")
        List<UUID> participantIds
) {}
