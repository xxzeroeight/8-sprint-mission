package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.dto.domain.UserStatusDto;
import com.sprint.mission.discodeit.domain.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.userstatus.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserSwaggerApi
{
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
                                                   @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException
    {
        log.debug("사용자 생성 시작(정보): username={}, email={}", userCreateRequest.username(), userCreateRequest.email());

        Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.empty();
        if (profile != null) {
            log.debug("사용자 생성 시작(프로필): fileName={}", profile.getOriginalFilename());

            binaryContentCreateRequest = Optional.of(new BinaryContentCreateRequest(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getBytes()
            ));
        }

        UserDto user = userService.create(userCreateRequest, binaryContentCreateRequest);

        log.info("사용자 생성 완료: userId={}", user.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId)
    {
        log.debug("사용자 조회(단건) 시작: userId={}", userId);

        UserDto user = userService.findById(userId);

        log.debug("사용자 조회(단건) 완료: userId={}", userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers()
    {
        log.debug("사용자 조회(다건) 시작");
        List<UserDto> users = userService.findAll();

        List<UserResponse> responses = users.stream()
                .map(UserResponse::from)
                .toList();

        log.debug("사용자 조회(다건) 완료");

        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
                                                   @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
                                                   @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException
    {
        log.debug("사용자 정보 수정 시작(정보): userId={}, newUsername={}, newEmail={}", userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail());

        Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.empty();
        if (profile != null) {
            log.debug("사용자 정보 수정 시작(이미지): fileName={}", profile.getOriginalFilename());

            binaryContentCreateRequest = Optional.of(new BinaryContentCreateRequest(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getBytes()
            ));
        }

        UserDto user = userService.update(userId, userUpdateRequest, binaryContentCreateRequest);

        log.info("사용자 정보 수정 완료: userId={}", user.id());

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId)
    {
        log.debug("사용자 삭제 시작: userId={}", userId);

        userService.delete(userId);

        log.info("사용자 삭제 완료: userId={}", userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResponse> updateStatus(@PathVariable UUID userId,
                                                           @Valid @RequestBody UserStatusUpdateRequest userStatusUpdateRequest)
    {
        log.debug("사용자 상태 수정 시작: userId={}, newLastActiveAt={}", userId, userStatusUpdateRequest.newLastActiveAt());

        UserStatusDto userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

        log.info("사용자 상태 수정 완료: userId={}", userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserStatusResponse.from(userStatus));
    }
}
