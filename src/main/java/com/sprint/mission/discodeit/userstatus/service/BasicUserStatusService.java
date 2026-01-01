package com.sprint.mission.discodeit.userstatus.service;

import com.sprint.mission.discodeit.userstatus.dto.domain.UserStatusDto;
import com.sprint.mission.discodeit.userstatus.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.userstatus.domain.UserStatus;
import com.sprint.mission.discodeit.user.exception.DuplicateUserException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService
{
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest) {
        if (!userRepository.existsById(userStatusCreateRequest.userId())) {
            throw UserNotFoundException.byId(userStatusCreateRequest.userId());
        }

        if (userStatusRepository.findByUserId(userStatusCreateRequest.userId()).isPresent()) {
            throw DuplicateUserException.byId(userStatusCreateRequest.userId());
        }

        UserStatus userStatus = new UserStatus(userStatusCreateRequest.userId(), userStatusCreateRequest.lastActiveAt());
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return UserStatusDto.from(savedUserStatus);
    }

    @Override
    public UserStatusDto find(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .map(UserStatusDto::from)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userStatusId));
    }

    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(UserStatusDto::from)
                .toList();
    }

    @Override
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userStatusId));

        userStatus.update(userStatusUpdateRequest.updateLastActiveAt());
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return UserStatusDto.from(savedUserStatus);
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userId));

        userStatus.update(userStatusUpdateRequest.updateLastActiveAt());
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return UserStatusDto.from(savedUserStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw UserStatusNotFoundException.byId(userStatusId);
        }

        userStatusRepository.deleteById(userStatusId);
    }
}
