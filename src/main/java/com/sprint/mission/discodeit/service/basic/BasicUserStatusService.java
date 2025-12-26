package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateUserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
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
