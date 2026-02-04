package com.sprint.mission.discodeit.global.secutiry.auth.service;

import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import com.sprint.mission.discodeit.global.secutiry.auth.dto.request.LoginRequest;
import com.sprint.mission.discodeit.global.secutiry.auth.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 유저(로그인): username={}", loginRequest.username());
                    return new UserNotFoundException(loginRequest.username());
                });

        if (!user.getPassword().equals(loginRequest.password())) {
            throw new PasswordMismatchException();
        }

        return userMapper.toDto(user);
    }
}
