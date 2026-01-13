package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.exception.DuplicateUserException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService
{
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        if (userRepository.existsByEmail(userCreateRequest.email())) {
            throw DuplicateUserException.byEmail(userCreateRequest.email());
        }

        if (userRepository.existsByUsername(userCreateRequest.username())) {
            throw DuplicateUserException.byUsername(userCreateRequest.username());
        }

        BinaryContent profile = createProfile(binaryContentCreateRequest);

        User user = new User(userCreateRequest.username(), userCreateRequest.password(), userCreateRequest.email(), profile);
        User createdUser = userRepository.save(user);

        Boolean online = getOnlineStatus(createdUser);

        return UserDto.from(createdUser, online);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Boolean online = getOnlineStatus(user);
                    return UserDto.from(user, online);
                })
                .orElseThrow(() -> UserNotFoundException.byId(userId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Boolean online = getOnlineStatus(user);
                    return UserDto.from(user, online);
                })
                .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        if (userRepository.existsByUsername(userUpdateRequest.newUsername())) {
            throw DuplicateUserException.byUsername(userUpdateRequest.newUsername());
        }

        if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
            throw DuplicateUserException.byEmail(userUpdateRequest.newEmail());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));

        BinaryContent profile = createProfile(binaryContentCreateRequest);
        user.update(userUpdateRequest.newUsername(), userUpdateRequest.newPassword(), userUpdateRequest.newEmail(), profile);

        Boolean online = getOnlineStatus(user);

        return UserDto.from(user, online);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> UserNotFoundException.byId(userId));

        // pofile 먼저 삭제.
        if (user.getProfile() != null) {
            binaryContentRepository.deleteById(user.getProfile().getId());
        }

        userRepository.deleteById(userId);
    }

    private Boolean getOnlineStatus(User user) {
        return user.getUserStatus().isOnline(); // 객체 그래프 탐색. (user와 userstatus는 cascade관계이므로 null값이 올 수 없음.)
    }

    private BinaryContent createProfile(Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        BinaryContent profile = null;

        if (binaryContentCreateRequest.isPresent()) {
            profile = new BinaryContent(
                    binaryContentCreateRequest.get().fileName(),
                    (long) binaryContentCreateRequest.get().bytes().length,
                    binaryContentCreateRequest.get().contentType(),
                    binaryContentCreateRequest.get().bytes()
            );

            binaryContentRepository.save(profile);
        }

        return profile;
    }
}
