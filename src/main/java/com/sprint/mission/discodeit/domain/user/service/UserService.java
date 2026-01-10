package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService
{
    UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest);
    UserDto findById(UUID userId);
    List<UserDto> findAll();
    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest);
    void delete(UUID userId);
}