package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService
{
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        log.debug("사용자 생성 처리 시작: email={}, username={}", userCreateRequest.email(), userCreateRequest.username());

        if (userRepository.existsByEmail(userCreateRequest.email())) {
            log.warn("이미 사용중인 이메일(생성): email={}", userCreateRequest.email());
            throw new UserAlreadyExistsException(userCreateRequest.email());
        }

        if (userRepository.existsByUsername(userCreateRequest.username())) {
            log.warn("이미 사용중인 닉네임(생성): username={}", userCreateRequest.username());
            throw new UserAlreadyExistsException(userCreateRequest.username());
        }

        BinaryContent profile = createProfile(binaryContentCreateRequest);

        User user = new User(userCreateRequest.username(), userCreateRequest.password(), userCreateRequest.email(), profile);
        User createdUser = userRepository.save(user);

        log.debug("사용자 생성 처리 완료: userId={}", createdUser.getId());

        return userMapper.toDto(createdUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> userMapper.toDto(user))
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자(단건 조회): userId={}", userId);
                    return new UserNotFoundException(userId);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> userMapper.toDto(user))
                .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        log.debug("사용자 정보 수정 시작: userId={}, username={}, email={}", userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail());
        if (userRepository.existsByUsername(userUpdateRequest.newUsername())) {
            log.warn("이미 사용중인 닉네임(수정): username={}", userUpdateRequest.newUsername());
            throw new UserAlreadyExistsException(userUpdateRequest.newUsername());
        }

        if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
            log.warn("이미 사용중인 이메일(수정): email={}", userUpdateRequest.newEmail());
            throw new UserAlreadyExistsException(userUpdateRequest.newEmail());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자(수정): userId={}", userId);
                    return new UserNotFoundException(userId);
                });

        BinaryContent profile = createProfile(binaryContentCreateRequest);
        user.update(userUpdateRequest.newUsername(), userUpdateRequest.newPassword(), userUpdateRequest.newEmail(), profile);

        log.debug("사용자 정보 수정 완료: userId={}, username={}, email={}", userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail());

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.debug("사용자 삭제 처리 시작: userId={}", userId);

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            log.warn("존재하지 않는 사용자(삭제): userId={}", userId);
                            return new UserNotFoundException(userId);
                        });

        // pofile 먼저 삭제.
        if (user.getProfile() != null) {
            binaryContentRepository.deleteById(user.getProfile().getId());
        }

        userRepository.delete(user);

        log.debug("사용자 삭제 처리 완료: userId={}", userId);
    }

    private BinaryContent createProfile(Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {
        return binaryContentCreateRequest.map(binaryContent -> {
                BinaryContent profile = binaryContentRepository.save(
                        new BinaryContent(
                                binaryContent.fileName(),
                                (long) binaryContent.bytes().length,
                                binaryContent.contentType())
                );
                binaryContentStorage.save(profile.getId(), binaryContent.bytes());
                return profile;
            }).orElse(null);
    }
}
