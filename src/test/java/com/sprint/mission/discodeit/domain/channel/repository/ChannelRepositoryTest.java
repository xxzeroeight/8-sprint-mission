package com.sprint.mission.discodeit.domain.channel.repository;

import com.sprint.mission.discodeit.config.TestJpaConfig;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
@DisplayName("ChannelRepository Slice Test")
class ChannelRepositoryTest
{
    @Autowired private UserRepository userRepository;
    @Autowired private ChannelRepository channelRepository;
    @Autowired private ReadStatusRepository readStatusRepository;

    private Channel publicChannel;
    private User user;

    @BeforeEach
    void setUp() {
        publicChannel = channelRepository.save(new Channel("general", "general", ChannelType.PUBLIC));
        user = userRepository.save(new User("test", "password", "test@naver.com", null));
    }

    @Test
    @DisplayName("성공: 채널 저장")
    void givenChannel_whenSave_thenIdExists() {
        // given
        Channel channel = new Channel("general", "general", ChannelType.PUBLIC);

        // when
        Channel savedChannel = channelRepository.save(channel);

        // then
        assertThat(savedChannel.getId()).isNotNull();
        assertThat(savedChannel.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("성공: 채널 조회")
    void givenChannleId_whenFind_thenReturnsChannel() {
        // given


        // when
        Optional<Channel> res = channelRepository.findById(publicChannel.getId());

        // then
        assertThat(res).isPresent().get()
                .satisfies(channel -> {
                    assertThat(channel.getName()).isEqualTo("general");
                    assertThat(channel.getType()).isEqualTo(ChannelType.PUBLIC);
                });
    }

    @Test
    @DisplayName("실패: 존재하지 않는 ID")
    void givenChannelId_whenFind_thenChannelDoesNotExist() {
        // given


        // when
        Optional<Channel> res = channelRepository.findById(UUID.randomUUID());

        // then
        assertThat(res).isEmpty();
    }

    @Test
    @DisplayName("성공: 특정 사용자의 모든 채널 조회")
    void givenUserId_whenFind_thenReturnsChannels() {
        // given
        Channel privateChannel =  channelRepository.save(new Channel("", "", ChannelType.PRIVATE));
        ReadStatus readStatus = readStatusRepository.save(new ReadStatus(user, privateChannel, privateChannel.getCreatedAt()));

        // when
        List<Channel> res = channelRepository.findAllAccessibleByUserId(user.getId());

        // then
        assertThat(res).hasSize(2);
        assertThat(res).extracting(Channel::getType)
                .containsExactlyInAnyOrder(ChannelType.PRIVATE, ChannelType.PUBLIC);
    }
}