package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Test")
class BasicUserServiceTest
{
    @InjectMocks private BasicUserService basicUserService;

    @Mock private UserRepository userRepository;
    @Mock private UserStatusRepository userStatusRepository;
    @Mock private BinaryContentRepository binaryContentRepository;
    @Mock private BinaryContentStorage binaryContentStorage;
    @Mock private UserMapper userMapper;

    private User user;
    private UserDto userDto;
    private UserCreateRequest userCreateRequest;
    private BinaryContent binaryContent;
    private BinaryContentDto binaryContentDto;
    private BinaryContentCreateRequest binaryContentCreateRequest;
    private byte[] bytes;

    @BeforeEach
    void setUp() {
        String username = "xxzeroeight";
        String email = "xxzeroeight@naver.com";
        String password = "password1234";

        String fileName = "profile";
        String contentType = "jpg";

        bytes = "hello world".getBytes();
        binaryContent = new BinaryContent(fileName, 11L, contentType);

        userCreateRequest = new UserCreateRequest(username, email, password);
        binaryContentCreateRequest = new BinaryContentCreateRequest(fileName, contentType, bytes);

        user = new User(username, password, email, binaryContent);

        binaryContentDto = new BinaryContentDto(UUID.randomUUID(), fileName, contentType, 11L);
        userDto = new UserDto(UUID.randomUUID(), binaryContentDto, username, email, true);
    }

    @Nested
    @DisplayName("Create User")
    class Create {
        @Test
        @DisplayName("유효한 정보(프로필 포함)로 유저를 생성한다")
        void create_WithValidInfo_SholudSaveUser() {
            // given
            given(userRepository.existsByEmail("xxzeroeight@naver.com")).willReturn(false);
            given(userRepository.existsByUsername("xxzeroeight")).willReturn(false);

            given(userRepository.save(any(User.class))).willReturn(user);
            given(userMapper.toDto(user)).willReturn(userDto);

            given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);
            given(binaryContentStorage.save(binaryContent.getId(), bytes)).willReturn(binaryContent.getId());


            // when
            UserDto res = basicUserService.create(userCreateRequest, Optional.of(binaryContentCreateRequest));

            // then
            then(userRepository).should().save(any(User.class));
            then(binaryContentRepository).should().save(any(BinaryContent.class));
            then(binaryContentStorage).should().save(eq(binaryContent.getId()), eq(bytes));
            then(userMapper).should().toDto(user);

            assertThat(res).isEqualTo(userDto);
            assertThat(res).isNotNull();
            assertThat(res.email()).isEqualTo(userCreateRequest.email());
            assertThat(res.username()).isEqualTo(userCreateRequest.username());

            assertThat(res.profile()).isEqualTo(binaryContentDto);
            assertThat(res.profile()).isNotNull();
            assertThat(res.profile().size()).isEqualTo(11L);
            assertThat(res.profile().fileName()).isEqualTo(binaryContentCreateRequest.fileName());
            assertThat(res.profile().contentType()).isEqualTo(binaryContentCreateRequest.contentType());
        }

        @Test
        @DisplayName("이미 존재하는 이메일로 가입 시도 시 예외가 발생한다")
        void create_DuplicateEmail_ThrowsUserAlreadyExistsException() {
            // given
            given(userRepository.existsByEmail(userCreateRequest.email())).willReturn(true);

            // when
            assertThatThrownBy(() -> basicUserService.create(userCreateRequest, Optional.of(binaryContentCreateRequest)))
                    .isInstanceOf(UserAlreadyExistsException.class);

            // then
            then(userRepository).should(never()).save(any(User.class));
            then(binaryContentRepository).should(never()).save(any(BinaryContent.class));
            then(binaryContentStorage).should(never()).save(any(), any());
            then(userMapper).should(never()).toDto(any(User.class));
        }
    }

    @Nested
    @DisplayName("Update User")
    class Update {
        @Test
        @DisplayName("유효한 정보로 사용자 정보를 수정한다")
        void update_WithValidInfo_UpdatesSuccessfully() {
            // given
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest("updatedUsername", "updatedEmail", "updatedPassword");

            given(userRepository.existsByUsername(userUpdateRequest.newUsername())).willReturn(false);
            given(userRepository.existsByEmail(userUpdateRequest.newEmail())).willReturn(false);
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

            UserDto updatedUserDto = new UserDto(user.getId(), null, "updatedUsername", "updatedEmail", true);
            given(userMapper.toDto(user)).willReturn(updatedUserDto);

            // when
            UserDto res = basicUserService.update(user.getId(), userUpdateRequest, Optional.empty());

            // then
            then(userRepository).should().findById(user.getId());
            then(userRepository).should().existsByEmail(userUpdateRequest.newEmail());
            then(userRepository).should().existsByUsername(userUpdateRequest.newUsername());
            then(userMapper).should().toDto(user);

            assertThat(res).isEqualTo(updatedUserDto);
            assertThat(res.email()).isEqualTo(updatedUserDto.email());
            assertThat(res.username()).isEqualTo(updatedUserDto.username());
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 예외가 발생한다")
        void findById_NonExistingId_ThrowsUserNotFoundException() {
            // given
            UUID userId = UUID.randomUUID();

            given(userRepository.existsByUsername(anyString())).willReturn(false);
            given(userRepository.existsByEmail(anyString())).willReturn(false);
            given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicUserService.update(userId,
                    new UserUpdateRequest("updatedUsername", "updatedEmail", "updatedPassword"), Optional.empty())
            ).isInstanceOf(UserNotFoundException.class);

            // then
            then(userRepository).should().existsByUsername(anyString());
            then(userRepository).should().existsByEmail(anyString());
            then(userRepository).should().findById(any(UUID.class));

            then(userMapper).should(never()).toDto(user);
        }
    }

    @Nested
    @DisplayName("Delete User")
    class Delete {
        @Test
        @DisplayName("사용자가 탈퇴하면 데이터가 삭제된다")
        void delete_ExistingId_RemovesSuccessfully() {
            // given
            given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));

            willDoNothing().given(binaryContentRepository).deleteById(user.getProfile().getId());
            willDoNothing().given(userRepository).delete(any(User.class));

            // when
            basicUserService.delete(UUID.randomUUID());

            // then
            then(userRepository).should().findById(any(UUID.class));
            then(binaryContentRepository).should().deleteById(user.getProfile().getId());
            then(userRepository).should().delete(any(User.class));
        }

        @Test
        @DisplayName("존재하지 않는 ID로 삭제하면 예외가 발생한다")
        void delete_NonExistingId_ThrowsUserNotFoundException() {
            // given
            given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicUserService.delete(UUID.randomUUID())).isInstanceOf(UserNotFoundException.class);

            // then
            then(userRepository).should().findById(any(UUID.class));
            then(binaryContentRepository).should(never()).deleteById(user.getProfile().getId());
            then(userRepository).should(never()).delete(any(User.class));
        }
    }
}