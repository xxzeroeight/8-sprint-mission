package com.sprint.mission.discodeit.domain.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChannelController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@DisplayName("ChannelController Slice Test")
class ChannelControllerTest
{
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private ChannelService channelService;

    private ChannelDto channelDto;
    private UUID channelId;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();

        channelDto = new ChannelDto(UUID.randomUUID(), "general", "general", ChannelType.PUBLIC, List.of(), Instant.now());
    }

    @Test
    @DisplayName("유효한 정보로 채널 생성 요청 시 201 CREATED를 반환한다")
    void create_WithValidInput_ReturnsCreated() throws Exception {
        // given
        PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest("general", "general");

        given(channelService.create(any(PublicChannelCreateRequest.class))).willReturn(channelDto);

        // when


        // then
        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(publicChannelCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(channelDto.name()));

        then(channelService).should().create(any(PublicChannelCreateRequest.class));
    }

    @Test
    @DisplayName("유효한 정보로 채널 수정 요성 시 200 OK를 반환한다")
    void update_WithValidInput_ReturnsOk() throws Exception {
        // given
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("updated", "updated");

        given(channelService.update(channelId, publicChannelUpdateRequest)).willReturn(channelDto);

        // when


        // then
        mockMvc.perform(put("/api/channels/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(publicChannelUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(channelDto.name()));
    }

    @Test
    @DisplayName("채널 데이터 삭제 요청 시 204 NO_CONTENT를 반환한다")
    void delete_ExistingId_ReturnsNoContent() throws Exception {
        // given


        // when


        // then
        mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                .andExpect(status().isNoContent());

        then(channelService).should().delete(channelId);
    }
}