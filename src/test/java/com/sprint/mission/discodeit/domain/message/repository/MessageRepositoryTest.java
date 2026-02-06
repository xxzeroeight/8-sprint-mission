package com.sprint.mission.discodeit.domain.message.repository;

import com.sprint.mission.discodeit.config.TestJpaConfig;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
@DisplayName("MessageRepository Slice Test")
class MessageRepositoryTest
{
    @Autowired private MessageRepository messageRepository;
    @Autowired private ChannelRepository channelRepository;
    @Autowired private UserRepository userRepository;

    private User user;
    private Channel channel;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("xxzeroeight", "password1234", "xxzeroeight@naver.com", null));
        channel = channelRepository.save(new Channel("general", "general", ChannelType.PUBLIC));
    }
    
    @Test
    @DisplayName("유효한 정보로 메시지 객체를 저장하면 ID가 생성된다")
    void save_WithValidInfo_AssignsId() {
        // given
        Message message = messageRepository.save(new Message(channel, user, "message", List.of()));
        
        // when
        Optional<Message> res = messageRepository.findById(message.getId());
        
        // then
        assertThat(res.isPresent());
        assertThat(res.get().getContent()).isEqualTo("message");
    }

    @Test
    @DisplayName("채널의 첫 페이지 메시지 목록을 조회한다")
    void findFirstPageByChannelId_ReturnsPagedMessages() {
        // given
        messageRepository.save(new Message(channel, user, "message1", List.of()));
        messageRepository.save(new Message(channel, user, "message2", List.of()));
        messageRepository.save(new Message(channel, user, "message3", List.of()));
        messageRepository.save(new Message(channel, user, "message4", List.of()));

        // when
        Pageable pageable = PageRequest.of(0, 3);
        Slice<Message> messageSlice = messageRepository.findFirstPageByChannelId(channel.getId(), pageable);

        // then
        assertThat(messageSlice.hasContent()).isTrue();
        assertThat(messageSlice.hasNext()).isTrue();
        assertThat(messageSlice.getContent())
                .extracting(Message::getContent)
                .hasSize(3);
    }

    @Test
    @DisplayName("메시지 객체를 삭제한다")
    void delete_ExistingId_DeletesSuccessfully() {
        // given
        Message message = messageRepository.save(new Message(channel, user, "message1", List.of()));
        
        // when
        messageRepository.deleteById(message.getId());
        
        // then
        assertThat(messageRepository.findById(message.getId()).isEmpty());
    }
}