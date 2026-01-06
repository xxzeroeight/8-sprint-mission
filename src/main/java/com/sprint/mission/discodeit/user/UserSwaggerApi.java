package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.userstatus.dto.response.UserStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "user", description = "user API")
public interface UserSwaggerApi
{
    // createUser
    @Operation(summary = "user 등록")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "user가 성공적으로 생성됨.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "email 또는 username 중복.",
                    content = @Content(examples = @ExampleObject(value = "eemail 또는 username 중복."))
            )
    })
    ResponseEntity<UserResponse> createUser(
            @Parameter(
                    description = "user 생성 정보(JSON)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ) UserCreateRequest userCreateRequest,
            @Parameter(
                    description = "User 프로필 이미지(form-data)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) MultipartFile profile
    ) throws IOException;

    // getUser
    @Operation(summary = "user 찾기(단건)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "user를 성공적으로 찾음.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "user를 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "user를 찾을 수 없음."))
            )
    })
    ResponseEntity<UserResponse> getUser(
            @Parameter(description = "조회할 user의 id") UUID userId
    );

    // getAllUsers
    @Operation(summary = "user 찾기(다건)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "user를 성공적으로 찾음.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            )
    })
    ResponseEntity<List<UserResponse>> getAllUsers();

    // updateUser
    @Operation(summary = "user 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "user가 성공적으로 수정됨.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "user를 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "user를 찾을 수 없음."))
            ),
            @ApiResponse(
                    responseCode = "409", description = "email 또는 username 중복.",
                    content = @Content(examples = @ExampleObject(value = "email 또는 username 중복."))
            )
    })
    ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "수정할 user의 id") UUID userId,
            @Parameter(
                    description = "수정할 user의 정보(JSON)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ) UserUpdateRequest userUpdateRequest,
            @Parameter(
                    description = "수정할 user의 프로필 이미지(form-data)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) MultipartFile profile
    ) throws IOException;

    // deleteUser
    @Operation(summary = "user 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "user가 성공적으로 삭제됨."),
            @ApiResponse(
                    responseCode = "404", description = "user를 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "user를 찾을 수 없음."))
            )
    })
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "삭제할 user의 id") UUID userId
    );

    // updateStatus
    @Operation(summary = "user 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "user의 상태가 성공적으로 변경됨.",
                    content = @Content(schema = @Schema(implementation = UserStatusResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "userStatus를 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "userStatus를 찾을 수 없음."))
            )
    })
    ResponseEntity<UserStatusResponse> updateStatus(
            @Parameter(description = "상태를 변경할 user의 id") UUID userId,
            @Parameter(description = "상태 변경 시간") UserStatusUpdateRequest userStatusUpdateRequest
    );
}
