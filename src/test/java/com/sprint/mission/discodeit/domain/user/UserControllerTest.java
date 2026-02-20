package com.sprint.mission.discodeit.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.dto.domain.UserStatusDto;
import com.sprint.mission.discodeit.domain.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import com.sprint.mission.discodeit.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@DisplayName("UserController Slice Test")
class UserControllerTest
{
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private UserService userService;
    @MockitoBean private UserStatusService userStatusService;

    private UUID userId;
    private UUID userStatusId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userStatusId = UUID.randomUUID();
    }

    @Test
    @DisplayName("성공: 유저 생성 (201)")
    void givenCreateRequest_whenMultipart_thenReturns201() throws Exception {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("test1", "test1@naver.com", "password1234");
        BinaryContentDto binaryContentDto = new BinaryContentDto(UUID.randomUUID(), "profile", "jpg", 3L);
        UserDto userDto = new UserDto(UUID.randomUUID(), binaryContentDto, "test1", "test1@naver.com", true);

        given(userService.create(any(UserCreateRequest.class), any())).willReturn(userDto);

        // 1. 유저 정보
        MockMultipartFile userCreateRequestMultiPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(userCreateRequest)
        );

        // 2. 프로필 이미지
        MockMultipartFile profileMultipart = new MockMultipartFile(
                "profile",
                "profile.jpg",
                "image/jpeg",
                "abc".getBytes()
        );

        // when


        // then
        mockMvc.perform(multipart("/api/users")
                        .file(userCreateRequestMultiPart)
                        .file(profileMultipart)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test1@naver.com"))
                .andExpect(jsonPath("$.profile").exists());

        then(userService).should().create(any(UserCreateRequest.class), any());
    }

    @Test
    @DisplayName("성공: 유저 상태 수정 (200)")
    void givenUpdateRequest_whenPatch_thenReturns200() throws Exception {
        // given
        UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());
        UserStatusDto userStatusDto = new UserStatusDto(userStatusId, userId, Instant.now());

        given(userStatusService.updateByUserId(userId, userStatusUpdateRequest)).willReturn(userStatusDto);

        // when


        // then
        mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userStatusUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        then(userStatusService).should().updateByUserId(userId, userStatusUpdateRequest);
    }
}