package com.sprint.mission.discodeit.domain.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@DisplayName("Channel Integration Test")
public class ChannelApiIntegrationTest
{
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private UserRepository userRepository;
    @Autowired private ChannelRepository channelRepository;

    @Test
    @DisplayName("성공: 채널 생성 및 DB 반영 확인")
    void givenCreateRequest_whenPost_thenChannelCreatedInDb() throws Exception {
        // given
        PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
                "general",
                "general"
        );

        // when


        // then
        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(publicChannelCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("general"));

        List<Channel> channels = channelRepository.findAll();
        assertThat(channels).hasSize(1);
        assertThat(channels.get(0).getName()).isEqualTo("general");
    }

    @Test
    @DisplayName("성공: 채널 수정 및 DB 반영 확인")
    void givenUpdateRequest_whenPut_thenChannelUpdatedInDb() throws Exception {
        // given
        Channel channel = channelRepository.save(new Channel("general", "general", ChannelType.PUBLIC));
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("tess1", "test1");

        // when


        // then
        mockMvc.perform(put("/api/channels/{channelId}", channel.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(publicChannelUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("tess1"));

        Optional<Channel> findedChannel = channelRepository.findById(channel.getId());
        assertThat(findedChannel.get().getName()).isEqualTo("tess1");
    }
}
