package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;

    @Transactional(readOnly = true)
    @Override
    public UserDto getCurrentUseInfo(DiscodeitUserDetails discodeitUserDetails) {
        if (discodeitUserDetails == null) {
            return null;
        }

        UUID userId = discodeitUserDetails.getUserDto().id();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다." + userId));

        return userMapper.toDto(user);
    }
}
