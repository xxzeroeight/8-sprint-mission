package com.sprint.mission.discodeit.domain.user.dto.domain;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.user.domain.Role;

import java.util.UUID;

public record UserDto(
        UUID id,
        BinaryContentDto profile,
        String username,
        String email,
        Boolean online,
        Role role
) {}
