package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.userstatus.dto.domain.UserStatusDto;
import com.sprint.mission.discodeit.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.userstatus.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController
{
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> createUser(@RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
                                                   @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException
    {
        Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.empty();
        if (profileImage != null) {
            binaryContentCreateRequest = Optional.of(new BinaryContentCreateRequest(
                    profileImage.getOriginalFilename(),
                    profileImage.getContentType(),
                    profileImage.getBytes()
            ));
        }

        UserDto user = userService.create(userCreateRequest, binaryContentCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId)
    {
        UserDto user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserDto> users = userService.findAll();

        List<UserResponse> responses = users.stream()
                .map(UserResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
                                                   @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
                                                   @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException
    {
        Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.empty();
        if (profileImage != null) {
            binaryContentCreateRequest = Optional.of(new BinaryContentCreateRequest(
                    profileImage.getOriginalFilename(),
                    profileImage.getContentType(),
                    profileImage.getBytes()
            ));
        }

        UserDto user = userService.update(userId, userUpdateRequest, binaryContentCreateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId)
    {
        userService.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserStatusResponse> updateUserOnlineStatusByUserId(@PathVariable UUID userId,
                                                                             @RequestBody UserStatusUpdateRequest userStatusUpdateRequest)
    {
        UserStatusDto userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserStatusResponse.from(userStatus));
    }
}
