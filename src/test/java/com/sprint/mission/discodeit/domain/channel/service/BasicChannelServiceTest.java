package com.sprint.mission.discodeit.domain.channel.service;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.domain.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChannelService Unit Test")
class BasicChannelServiceTest
{
    @InjectMocks private BasicChannelService basicChannelService;

    @Mock private ChannelRepository channelRepository;
    @Mock private ChannelMapper channelMapper;
    @Mock private UserRepository userRepository;

    private User user;
    private Channel channel;
    private ChannelDto channelDto;
    private PublicChannelCreateRequest publicChannelCreateRequest;

    private UUID channelId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        userId = UUID.randomUUID();

        String name = "testChannel";
        String description = "this is testChannel";
        ChannelType type = ChannelType.PUBLIC;

        user = new User("xxzeroeight", "password1234", "xxzeroeight@naver.com", null);

        channel = new Channel(name, description, type);
        publicChannelCreateRequest = new PublicChannelCreateRequest(name, description);

        channelDto = new ChannelDto(
                UUID.randomUUID(),
                "testChannel",
                "this is testChannel",
                ChannelType.PUBLIC,
                new ArrayList<UserDto>(),
                Instant.MIN
        );
    }

    @Nested
    @DisplayName("Create Channel")
    class Create {
        @Test
        @DisplayName("성공: 공개 채널 생성")
        void givenPublicCreateRequest_whenCreate_thenChannelCreated() {
            // given
            given(channelRepository.save(any(Channel.class))).willReturn(channel);
            given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

            // when
            ChannelDto res = basicChannelService.create(publicChannelCreateRequest);

            // then
            then(channelRepository).should().save(any(Channel.class));
            then(channelMapper).should().toDto(any(Channel.class));

            assertThat(res).isEqualTo(channelDto);
            assertThat(res.type()).isEqualTo(ChannelType.PUBLIC);
            assertThat(res.name()).isEqualTo("testChannel");
            assertThat(res.description()).isEqualTo("this is testChannel");
        }

        @Test
        @DisplayName("성공: 비공개 채널 생성")
        void givenPrivateCreateRequest_whenCreate_thenChannelCreated() {
            // given
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            Channel privateChannel = new Channel("", "", ChannelType.PRIVATE);

            ChannelDto privateChannelDto = new ChannelDto(
                    UUID.randomUUID(),
                    "",
                    "",
                    ChannelType.PRIVATE,
                    new ArrayList<UserDto>(),
                    Instant.MIN
            );

            PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(List.of(userId));

            given(channelRepository.save(any(Channel.class))).willReturn(privateChannel);
            given(channelMapper.toDto(any(Channel.class))).willReturn(privateChannelDto);

            // when
            ChannelDto res = basicChannelService.create(privateChannelCreateRequest);

            // then
            then(userRepository).should().findById(userId);
            then(channelRepository).should().save(any(Channel.class));
            then(channelMapper).should().toDto(any(Channel.class));

            assertThat(res).isEqualTo(privateChannelDto);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID")
        void givenNonExistingId_whenCreate_thenThrowsException() {
            // given
            PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(List.of(userId));

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicChannelService.create(privateChannelCreateRequest))
                    .isInstanceOf(UserNotFoundException.class);

            // then
            then(channelRepository).should(never()).save(any(Channel.class));
            then(channelMapper).should(never()).toDto(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("Update Channel")
    class Update {
        @Test
        @DisplayName("성공: 채널 수정")
        void givenUpdateRequest_whenUpdate_thenChannelUpdated() {
            // given
            PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("updatedChannelName", "updatedTestDescription");

            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

            ChannelDto updatedChannelDto = new ChannelDto(UUID.randomUUID(), "updatedChannelName", "updatedTestDescription", ChannelType.PUBLIC, new ArrayList<UserDto>(),  Instant.MIN);
            given(channelMapper.toDto(channel)).willReturn(updatedChannelDto);

            // when
            ChannelDto res = basicChannelService.update(channelId, publicChannelUpdateRequest);

            // then
            then(channelRepository).should().findById(channelId);
            then(channelMapper).should().toDto(channel);

            assertThat(res).isEqualTo(updatedChannelDto);
            assertThat(res.type()).isEqualTo(ChannelType.PUBLIC);
            assertThat(res.name()).isEqualTo("updatedChannelName");
            assertThat(res.description()).isEqualTo("updatedTestDescription");
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID")
        void givenNonExistingId_whenUpdate_thenThrowsException() {
            // given
            PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("updatedChannelName", "updatedTestDescription");

            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicChannelService.update(channelId, publicChannelUpdateRequest))
                    .isInstanceOf(ChannelNotFoundException.class);

            // then
            then(channelRepository).should().findById(channelId);
            then(channelMapper).should(never()).toDto(channel);
        }
    }

    @Nested
    @DisplayName("Delete Channel")
    class Delete {
        @Test
        @DisplayName("성공: 채널 삭제")
        void givenChannelId_whenDelete_thenChannelDeleted() {
            // given
            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

            // when
            basicChannelService.delete(channelId);

            // then
            then(channelRepository).should().findById(channelId);
            then(channelRepository).should().delete(channel);
        }

        @Test
        @DisplayName("실패: 채널 삭제")
        void givenNonExistingId_whenDelete_thenThrowsException() {
            // given
            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicChannelService.delete(channelId))
                    .isInstanceOf(ChannelNotFoundException.class);

            // then
            then(channelRepository).should().findById(channelId);
            then(channelRepository).should(never()).delete(channel);
        }
    }
}