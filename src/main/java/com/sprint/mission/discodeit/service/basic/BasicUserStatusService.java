package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateUserException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserStatusNotFoundException;
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
    public UserStatus create(UserStatusCreateRequest userStatusCreateRequest) {
        if (!userRepository.existsById(userStatusCreateRequest.userId())) {
            throw UserNotFoundException.byId(userStatusCreateRequest.userId());
        }

        if (userStatusRepository.findByUserId(userStatusCreateRequest.userId()).isPresent()) {
            throw DuplicateUserException.byId(userStatusCreateRequest.userId());
        }

        UserStatus userStatus = new UserStatus(userStatusCreateRequest.userId(), userStatusCreateRequest.lastActiveAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userStatusId));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream().toList();
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userStatusId));

        userStatus.update(userStatusUpdateRequest.updateLastActiveAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userId));

        userStatus.update(userStatusUpdateRequest.updateLastActiveAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw UserStatusNotFoundException.byId(userStatusId);
        }

        userStatusRepository.deleteById(userStatusId);
    }
}
