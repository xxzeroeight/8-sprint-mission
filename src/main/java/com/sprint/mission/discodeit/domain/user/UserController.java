package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.domain.user.service.UserService;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
                                                   @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException
    {
        log.debug("사용자 생성 시작(정보): username={}", userCreateRequest.username());

        Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.empty();
        if (profile != null) {
            log.debug("사용자 생성 시작(프로필): contentType={}", profile.getContentType());

            binaryContentCreateRequest = Optional.of(new BinaryContentCreateRequest(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getBytes()
            ));
        }

        UserDto user = userService.create(userCreateRequest, binaryContentCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId)
    {
        log.debug("사용자 조회(단건) 시작: userId={}", userId);

        UserDto user = userService.findById(userId);

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

        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
                                                   @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
                                                   @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException
    {
        log.debug("사용자 정보 수정 시작(정보): userId={}, newUsername={}", userId, userUpdateRequest.newUsername());

        Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.empty();
        if (profile != null) {
            log.debug("사용자 정보 수정 시작(이미지): contentType={}", profile.getContentType());

            binaryContentCreateRequest = Optional.of(new BinaryContentCreateRequest(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getBytes()
            ));
        }

        UserDto user = userService.update(userId, userUpdateRequest, binaryContentCreateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId)
    {
        log.debug("사용자 삭제 시작: userId={}", userId);

        userService.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
