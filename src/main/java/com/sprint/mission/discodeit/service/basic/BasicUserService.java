package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateUserException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
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

        return toDto(createdUser);
    }

    @Override
    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> toDto(user))
                .orElseThrow(() -> UserNotFoundException.byId(userId));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> toDto(user))
                .toList();
    }

    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        if (userRepository.existsByUsername(userUpdateRequest.updateUsername())) {
            throw DuplicateUserException.byUsername(userUpdateRequest.updateUsername());
        }

        if (userRepository.existsByEmail(userUpdateRequest.updateEmail())) {
            throw DuplicateUserException.byEmail(userUpdateRequest.updateEmail());
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

        user.update(userUpdateRequest.updateUsername(), userUpdateRequest.updatePassword(), userUpdateRequest.updateEmail(), profilId);
        userRepository.save(user);

        return toDto(user);
    }

    @Override
    public UserDto updateOnlineStatus(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> UserStatusNotFoundException.byId(userId));

        userStatus.update(Instant.now());
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);

        return toDto(user);
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

    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(userStatus -> userStatus.isOnline())
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getProfileId(),
                user.getUsername(),
                user.getEmail(),
                online,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
