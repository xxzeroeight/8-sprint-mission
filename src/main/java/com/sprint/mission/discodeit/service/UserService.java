package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;

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