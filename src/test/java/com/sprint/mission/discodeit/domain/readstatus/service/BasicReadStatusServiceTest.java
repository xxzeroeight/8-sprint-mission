package com.sprint.mission.discodeit.domain.readstatus.service;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.dto.domain.ReadStatusDto;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.readstatus.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.domain.readstatus.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReadStatusService Unit Test")
class BasicReadStatusServiceTest
{
    @InjectMocks BasicReadStatusService basicReadStatusService;

    @Mock private ReadStatusRepository readStatusRepository;
    @Mock private ReadStatusMapper readStatusMapper;
    @Mock private UserRepository userRepository;
    @Mock private ChannelRepository channelRepository;

    private UUID userId;
    private UUID channelId;
    private User user;
    private Channel channel;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        channelId = UUID.randomUUID();
        user = new User("test1", "password", "test1@naver.com", null);
        channel = new Channel("general", "general", ChannelType.PUBLIC);
    }

    @Nested
    @DisplayName("Create ReadStatus")
    class Create {
        @Test
        @DisplayName("성공: 읽음 상태 생성")
        void givenCreateRequest_whenCreate_thenReadStatusCreated() {
            // given
            ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(userId, channelId, Instant.now());
            ReadStatus savedReadStatus = new ReadStatus(user, channel, readStatusCreateRequest.lastReadAt());
            ReadStatusDto readStatusDto = new ReadStatusDto(savedReadStatus.getId(), userId, channelId, readStatusCreateRequest.lastReadAt());

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
            given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of());
            given(readStatusRepository.save(any(ReadStatus.class))).willReturn(savedReadStatus);
            given(readStatusMapper.toDto(savedReadStatus)).willReturn(readStatusDto);

            // when
            ReadStatusDto res = basicReadStatusService.create(readStatusCreateRequest);

            // then
            then(userRepository).should().findById(userId);
            then(channelRepository).should().findById(channelId);
            then(readStatusRepository).should().save(any(ReadStatus.class));
            then(readStatusMapper).should().toDto(savedReadStatus);

            assertThat(res).isEqualTo(readStatusDto);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 사용자 ID")
        void givenNoneExistingUserId_whenCreate_thenThrowsException() {
            // given
            ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(userId, channelId, Instant.now());

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicReadStatusService.create(readStatusCreateRequest))
                    .isInstanceOf(UserNotFoundException.class);

            // then
            then(userRepository).should().findById(userId);
            then(channelRepository).should(never()).findById(channelId);
            then(readStatusRepository).should(never()).save(any(ReadStatus.class));
            then(readStatusMapper).should(never()).toDto(any(ReadStatus.class));
        }
    }

    @Nested
    @DisplayName("Update ReadStatus")
    class Update {
        @Test
        @DisplayName("성공: 읽음 상태 수정")
        void givenUpdateRequest_whenUpdate_thenReadStatusUpdated() {
            // given
            UUID readStatusId = UUID.randomUUID();

            ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
            ReadStatusUpdateRequest readStatusUpdateRequest = new ReadStatusUpdateRequest(Instant.now());
            ReadStatus savedReadStatus = new ReadStatus(user, channel, readStatusUpdateRequest.newLastReadAt());
            ReadStatusDto readStatusDto = new ReadStatusDto(savedReadStatus.getId(), userId, channelId, readStatusUpdateRequest.newLastReadAt());

            given(readStatusRepository.findById(readStatusId)).willReturn(Optional.of(readStatus));
            given(readStatusRepository.save(any(ReadStatus.class))).willReturn(savedReadStatus);
            given(readStatusMapper.toDto(savedReadStatus)).willReturn(readStatusDto);

            // when
            ReadStatusDto res = basicReadStatusService.update(readStatusId, readStatusUpdateRequest);

            // then
            then(readStatusRepository).should().findById(readStatusId);
            then(readStatusRepository).should().save(readStatus);
            then(readStatusMapper).should().toDto(savedReadStatus);

            assertThat(res).isEqualTo(readStatusDto);
        }
    }

    @Nested
    @DisplayName("Delete ReadStatus")
    class Delete {
        @Test
        @DisplayName("성공: 읽음 상태 삭제")
        void givenExistingId_whenDelete_thenReadStatusDeleted() {
            // given
            UUID readStatusId = UUID.randomUUID();
            ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());

            given(readStatusRepository.findById(any(UUID.class))).willReturn(Optional.of(readStatus));

            // when
            basicReadStatusService.delete(readStatusId);

            // then
            then(readStatusRepository).should().findById(readStatusId);
            then(readStatusRepository).should().delete(any(ReadStatus.class));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID")
        void givenNonExistingId_whenDelete_thenThrowsException() {
            // given
            UUID readStatusId = UUID.randomUUID();

            given(readStatusRepository.findById(any(UUID.class))).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicReadStatusService.delete(readStatusId))
                    .isInstanceOf(ReadStatusNotFoundException.class);

            // then
            then(readStatusRepository).should().findById(readStatusId);
            then(readStatusRepository).should(never()).delete(any(ReadStatus.class));
        }
    }
}