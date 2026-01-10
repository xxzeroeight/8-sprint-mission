package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import com.sprint.mission.discodeit.domain.user.exception.DuplicateUserException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService
{
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        if (userRepository.existsByEmail(userCreateRequest.email())) {
            throw DuplicateUserException.byEmail(userCreateRequest.email());
        }

        if (userRepository.existsByUsername(userCreateRequest.username())) {
            throw DuplicateUserException.byUsername(userCreateRequest.username());
        }

        UUID profileId = null;
        if (binaryContentCreateRequest.isPresent()) {
            BinaryContent binaryContent = new BinaryContent(
                    binaryContentCreateRequest.get().fileName(),
                    (long) binaryContentCreateRequest.get().bytes().length,
                    binaryContentCreateRequest.get().contentType(),
                    binaryContentCreateRequest.get().bytes()
            );

            profileId = binaryContentRepository.save(binaryContent).getId();
        }

        User user = new User(userCreateRequest.username(), userCreateRequest.password(), userCreateRequest.email(), profileId);
        User createdUser = userRepository.save(user);

        UserStatus userStatus = new UserStatus(createdUser.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        Boolean online = getOnlineStatus(createdUser.getId());

        return UserDto.from(createdUser, online);
    }

    @Override
    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Boolean online = getOnlineStatus(user.getId());
                    return UserDto.from(user, online);
                })
                .orElseThrow(() -> UserNotFoundException.byId(userId));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Boolean online = getOnlineStatus(user.getId());
                    return UserDto.from(user, online);
                })
                .toList();
    }

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

        UUID profilId = null;
        if (binaryContentCreateRequest.isPresent()) {
            BinaryContent binaryContent = new BinaryContent(
                    binaryContentCreateRequest.get().fileName(),
                    (long) binaryContentCreateRequest.get().bytes().length,
                    binaryContentCreateRequest.get().contentType(),
                    binaryContentCreateRequest.get().bytes()
            );

            profilId = binaryContentRepository.save(binaryContent).getId();
        }

        user.update(userUpdateRequest.newUsername(), userUpdateRequest.newPassword(), userUpdateRequest.newEmail(), profilId);
        userRepository.save(user);

        Boolean online = getOnlineStatus(user.getId());

        return UserDto.from(user, online);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> UserNotFoundException.byId(userId));

        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }

        userStatusRepository.deleteByUserId(userId);
        userRepository.delete(userId);
    }

    private Boolean getOnlineStatus(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .map(UserStatus::isOnline)
                .orElse(null);
    }
}
