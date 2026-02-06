package com.sprint.mission.discodeit.domain.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@DisplayName("Message Integration Test")
public class MessageApiIntegrationTest
{
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private MessageRepository messageRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ChannelRepository channelRepository;

    @Test
    @DisplayName("성공: 메시지 생성 및 DB 반영 확인")
    void givenCreateRequest_whenMultipart_thenMessageCreatedInDb() throws Exception {
        // given
        User user = userRepository.save(new User("test1", "password1234", "test1@naver.com", null));
        Channel channel = channelRepository.save(new Channel("general", "general", ChannelType.PUBLIC));

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(channel.getId(), user.getId(), "message");

        MockMultipartFile messageCreateRequestMultipart = new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(messageCreateRequest)
        );

        MockMultipartFile attachmentMultipart = new MockMultipartFile(
                "attachments",
                "file.jpg",
                "image/jpeg",
                "abc".getBytes()
        );

        // when


        // then
        mockMvc.perform(multipart("/api/messages")
                .file(messageCreateRequestMultipart)
                .file(attachmentMultipart)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("message"));

        Message message = messageRepository.findAll().get(0);
        assertThat(message).isNotNull();
        assertThat(message.getContent()).isEqualTo("message");
        assertThat(message.getAttachments()).hasSize(1);
    }

    @Test
    @DisplayName("성공: 메시지 삭제 및 DB 반영 확인")
    void givenId_whenDelete_thenMessageDeletedInDb() throws Exception {
        // given
        User user = userRepository.save(new User("test1", "password1234", "test1@naver.com", null));
        Channel channel = channelRepository.save(new Channel("general", "general", ChannelType.PUBLIC));
        Message message = messageRepository.save(new Message(channel, user, "message", List.of()));

        // when


        // then
        mockMvc.perform(delete("/api/messages/{messageId}", message.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        boolean exists = messageRepository.existsById(message.getId());
        assertThat(exists).isFalse();
    }
}
