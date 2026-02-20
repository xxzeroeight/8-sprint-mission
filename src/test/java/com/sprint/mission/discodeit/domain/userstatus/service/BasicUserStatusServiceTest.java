package com.sprint.mission.discodeit.domain.userstatus.service;

import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.dto.domain.UserStatusDto;
import com.sprint.mission.discodeit.domain.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.userstatus.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserStatusService Unit Test")
class BasicUserStatusServiceTest
{
    @InjectMocks BasicUserStatusService basicUserStatusService;

    @Mock UserRepository userRepository;
    @Mock UserStatusRepository userStatusRepository;
    @Mock UserStatusMapper userStatusMapper;

    private User user;
    private UserStatus userStatus;

    @BeforeEach
    void setUp() {
        user = new User("test1", "password", "test1@naver.com", null);
        userStatus = new UserStatus(user, Instant.now());
    }

    @Test
    @DisplayName("성공: 유저 상태 변경")
    void givenUpdateRequest_whenUpdate_thenUserStatusUpdated() {
        // given
        UUID userId = UUID.randomUUID();

        UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());
        given(userStatusRepository.findByUserId(userId)).willReturn(Optional.of(userStatus));

        UserStatusDto updatedUserStatusDto = new UserStatusDto(UUID.randomUUID(), userId, Instant.now());
        given(userStatusMapper.toDto(userStatus)).willReturn(updatedUserStatusDto);

        // when
        UserStatusDto res = basicUserStatusService.updateByUserId(userId, userStatusUpdateRequest);

        // then
        then(userStatusRepository).should().findByUserId(userId);
        then(userStatusMapper).should().toDto(userStatus);

        assertThat(res).isEqualTo(updatedUserStatusDto);
    }
}