package com.sprint.mission.discodeit.domain.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
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
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@DisplayName("MessageController Slice Test")
class MessageControllerTest
{
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private MessageService messageService;

    private UUID userId;
    private UUID channelId;
    private UUID messageId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        channelId = UUID.randomUUID();
        messageId = UUID.randomUUID();
    }

    @Test
    @DisplayName("유효한 정보로 메시지 생성 요청 시 201 Created를 반환한다")
    void create_WithValidInput_ReturnsCreated() throws Exception {
        // given
        UserDto userDto = new UserDto(userId, null, "test1", "test1@naver.com", true);
        MessageDto messageDto = new MessageDto(messageId, channelId, userDto, List.of(), "message", Instant.now(), Instant.now());

        given(messageService.create(any(MessageCreateRequest.class), any())).willReturn(messageDto);

        MockMultipartFile messageCreateRequestMultiPart = new MockMultipartFile(
                "messageCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(new MessageCreateRequest(channelId, userId, "message"))
        );

        // when


        // then
        mockMvc.perform(multipart("/api/messages")
                .file(messageCreateRequestMultiPart)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content").value(messageDto.content()));
    }

    @Test
    @DisplayName("존재하지 않는 메시지 ID로 삭제 요청 시 에러를 반환한다")
    void delete_NonExistingId_ReturnsMessageNotFoundException() throws Exception {
        // given
        willThrow(new MessageNotFoundException(messageId)).given(messageService).delete(messageId);

        // when


        // then
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                .andExpect(status().isNotFound());

        then(messageService).should().delete(messageId);
    }

    @Test
    @DisplayName("메시지 데이터 삭제 요청 시 204 NO_CONTENT를 반환한다")
    void delete_ExistinId_ReturnsNoContent() throws Exception {
        // given


        // when


        // then
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                .andExpect(status().isNoContent());

        then(messageService).should().delete(messageId);
    }
}