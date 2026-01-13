package com.sprint.mission.discodeit.domain.userstatus.service;

import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.dto.domain.UserStatusDto;
import com.sprint.mission.discodeit.domain.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService
{
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    // UserRepository.findById()로 User -> UserStatus로 접근할 수 있으므로 사실상 UserService로 통합 가능함.
    @Transactional
    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userId));

        userStatus.update(userStatusUpdateRequest.newLastActiveAt());

        return UserStatusDto.from(userStatus);
    }
}
