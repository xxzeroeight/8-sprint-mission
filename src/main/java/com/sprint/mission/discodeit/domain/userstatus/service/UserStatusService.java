package com.sprint.mission.discodeit.domain.userstatus.service;

import com.sprint.mission.discodeit.domain.userstatus.dto.domain.UserStatusDto;
import com.sprint.mission.discodeit.domain.userstatus.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.domain.userstatus.dto.request.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService
{
    UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest);
    UserStatusDto find(UUID userStatusId);
    List<UserStatusDto> findAll();
    UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest);
    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);
    void delete(UUID userStatusId);
}
