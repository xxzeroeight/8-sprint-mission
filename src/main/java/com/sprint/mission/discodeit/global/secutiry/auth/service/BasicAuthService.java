package com.sprint.mission.discodeit.global.secutiry.auth.service;

import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import com.sprint.mission.discodeit.global.secutiry.auth.dto.request.LoginRequest;
import com.sprint.mission.discodeit.global.secutiry.auth.exception.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService
{
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> UserNotFoundException.byUsername(loginRequest.username()));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw InvalidPasswordException.incorrect();
        }

        return userMapper.toDto(user);
    }
}
