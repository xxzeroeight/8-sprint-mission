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
    @DisplayName("유효한 정보로 채널 객체를 저장하면 ID가 생성된다")
    void save_WithValidInfo_AssignsId() {
        // given
        Channel channel = new Channel("general", "general", ChannelType.PUBLIC);

        // when
        Channel savedChannel = channelRepository.save(channel);

        // then
        assertThat(savedChannel.getId()).isNotNull();
        assertThat(savedChannel.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("존재하는 채널 ID로 조회하면 채널 정보를 반환한다")
    void findById_ExistingId_ReturnsChannelInfo() {
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
    @DisplayName("존재하지 않는 채널 ID로 조회하면 빈 값이 반환된다")
    void findById_NonExistingId_ReturnsNull() {
        // given


        // when
        Optional<Channel> res = channelRepository.findById(UUID.randomUUID());

        // then
        assertThat(res).isEmpty();
    }

    @Test
    @DisplayName("사용자가 참여 중인 모든 채널의 개수를 반환한다")
    void findALl_ExistingChannel_ReturnsChannelCounts() {
        // given
        Channel privateChannel =  channelRepository.save(new Channel("", "", ChannelType.PRIVATE));
        ReadStatus readStatus = readStatusRepository.save(new ReadStatus(user, privateChannel, privateChannel.getCreatedAt()));

        // when
        List<Channel> res = channelRepository.findAllAccessibleByUserId(user.getId());

        // then
        assertThat(res).hasSize(2);
        assertThat(res).extracting(Channel::getType)
                .containsExactly(ChannelType.PRIVATE, ChannelType.PUBLIC);
    }
}