package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
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

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> createUser(@RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
                                                   @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id)
    {
        UserDto user = userService.findById(id);

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

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id,
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

        UserDto user = userService.update(id, userUpdateRequest, binaryContentCreateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(user));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateUserOnlineStatus(@PathVariable UUID id)
    {
        UserDto user = userService.updateOnlineStatus(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id)
    {
        userService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
