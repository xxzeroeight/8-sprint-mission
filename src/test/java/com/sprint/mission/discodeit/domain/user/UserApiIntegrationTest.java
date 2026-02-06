package com.sprint.mission.discodeit.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@DisplayName("User Integration Test")
public class UserApiIntegrationTest
{
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private UserRepository userRepository;
    @Autowired private UserStatusRepository userStatusRepository;
    @Autowired private BinaryContentRepository binaryContentRepository;

    @Test
    @DisplayName("유효한 정보(프로필 포함)로 사용자 생성 요청 시 201 CREATED를 반환한다")
    void create_WithValidInfo_ReturnsCreated() throws Exception {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest("test1", "test1@naver.com", "password1234");

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test1@naver.com"))
                .andExpect(jsonPath("$.profile").exists());

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("test1@naver.com");

        BinaryContent binaryContent = binaryContentRepository.findById(users.get(0).getProfile().getId())
                .orElseThrow();
        assertThat(binaryContent).isNotNull();
        assertThat(binaryContent.getFileName()).isEqualTo("profile.jpg");
    }

    @Test
    @DisplayName("유효한 정보로 유저 상태 변경 요청 시 200 OK를 반환한다")
    void update_WithValidInput_ReturnsOk() throws Exception {
        // given
        User user = userRepository.save(new User("xxzeroeight", "password1234", "xxzeroeight@naver.com", null));
        UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());

        // when


        // then
        mockMvc.perform(patch("/api/users/{userId}/userStatus", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userStatusUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(user.getId().toString()));

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElseThrow(() ->
                new UserStatusNotFoundException(user.getId()));

        assertThat(userStatus.getUser().getId()).isEqualTo(user.getId());
        assertThat(userStatus.getLastActiveAt()).isNotNull();
    }
}
