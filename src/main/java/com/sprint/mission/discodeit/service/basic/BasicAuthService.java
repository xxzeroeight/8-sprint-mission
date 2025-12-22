package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidPasswordException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService
{
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserDto login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> UserNotFoundException.byUsername(loginRequest.username()));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw InvalidPasswordException.incorrect();
        }

        return toDto(user);
    }

    private UserDto toDto(User user) {
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElse(null);

        return new UserDto(
                user.getId(),
                user.getProfileId(),
                user.getUsername(),
                user.getEmail(),
                userStatus != null ? userStatus.isOnline() : false,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
