package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.domain.Message;
import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.domain.message.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageService Unit Test")
class BasicMessageServiceTest
{
    @InjectMocks
    private BasicMessageService basicMessageService;

    @Mock private MessageRepository messageRepository;
    @Mock private MessageMapper messageMapper;
    @Mock private ChannelRepository channelRepository;
    @Mock private UserRepository userRepository;
    @Mock private BinaryContentRepository binaryContentRepository;
    @Mock private BinaryContentStorage binaryContentStorage;
    @Mock private PageResponseMapper pageResponseMapper;

    private User user;
    private UserDto userDto;
    private Channel channel;
    private BinaryContent binaryContent;
    private BinaryContentDto binaryContentDto;
    private byte[] bytes;

    private UUID channelId;
    private UUID messageId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        userId = UUID.randomUUID();
        messageId = UUID.randomUUID();

        bytes = "hello".getBytes();
        binaryContent = new BinaryContent("file", 3L, "jpg");

        channel = new Channel("channel", "description", ChannelType.PUBLIC);
        user = new User("xxzeroeight", "password1234", "xxzeroeight@naver.com", null);

        userDto = new UserDto(UUID.randomUUID(), null,"xxzeroeight","xxzeroeight@naver.com", true);
        binaryContentDto = new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getSize()
        );
    }

    @Nested
    @DisplayName("Create Message")
    class Create {
        @Test
        @DisplayName("성공: 메시지 생성")
        void givenCreateRequest_whenCreate_thenMessageCreated() {
            // given
            UserDto userDto = new UserDto(user.getId(), null, "xxzeroeight", "xxzeroeight@naver.com", true);

            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest("file", "jpg", bytes);

            given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);
            given(binaryContentStorage.save(binaryContent.getId(), bytes)).willReturn(binaryContent.getId());

            Message message = new Message(channel, user, "message", List.of(binaryContent));
            given(messageRepository.save(any(Message.class))).willReturn(message);

            MessageCreateRequest messageCreateRequest = new MessageCreateRequest(channelId, userId, "message");

            MessageDto messageDto = new MessageDto(
                    message.getId(),
                    channelId,
                    userDto,
                    List.of(binaryContentDto),
                    "message",
                    Instant.MIN,
                    Instant.MIN
            );

            given(messageMapper.toDto(message)).willReturn(messageDto);

            // when
            MessageDto res = basicMessageService.create(messageCreateRequest, List.of(binaryContentCreateRequest));

            // then
            then(channelRepository).should().findById(channelId);
            then(userRepository).should().findById(userId);
            then(binaryContentRepository).should().save(any(BinaryContent.class));
            then(binaryContentStorage).should().save(binaryContent.getId(), bytes);
            then(messageRepository).should().save(any(Message.class));
            then(messageMapper).should().toDto(message);

            assertThat(res).isEqualTo(messageDto);
            assertThat(res.content()).isEqualTo("message");
            assertThat(res.channelId()).isEqualTo(channelId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID")
        void givenNonExistingId_whenCreate_thenThrowsException() {
            // given
            MessageCreateRequest messageCreateRequest = new MessageCreateRequest(channelId, userId, "message");

            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicMessageService.create(messageCreateRequest, List.of()))
                    .isInstanceOf(ChannelNotFoundException.class);

            // then
            then(channelRepository).should().findById(channelId);
            then(userRepository).should(never()).findById(any());
            then(binaryContentRepository).should(never()).save(any());
            then(binaryContentStorage).should(never()).save(any(), any());
            then(messageRepository).should(never()).save(any());
            then(messageMapper).should(never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Find Message")
    class Find {
        @Test
        @DisplayName("성공: 메시지 조회")
        void givenId_whenFind_thenMessageFound() {
            // given
            Message message = new Message(channel, user, "message", List.of(binaryContent));
            MessageDto messageDto = new MessageDto(UUID.randomUUID(), channelId, null, List.of(), "message", Instant.now(), Instant.now());
            
            given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
            given(messageMapper.toDto(message)).willReturn(messageDto);

            // when
            MessageDto res = basicMessageService.findById(messageId);

            // then
            assertThat(res).isNotNull();
            assertThat(res.id()).isEqualTo(messageDto.id());
            
            verify(messageRepository).findById(messageId);
            verify(messageMapper).toDto(message);
        }

        @Test
        @DisplayName("성공: 메시지 조회 (다건, 첫 페이지)")
        void givenNullCursor_whenFind_thenFindFirstPage() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            Slice<Message> slice = new SliceImpl<>(List.of(), pageable, false);

            given(messageRepository.findFirstPageByChannelId(eq(channelId), any())).willReturn(slice);
            given(pageResponseMapper.fromSlice(any(), any(), anyBoolean())).willReturn(PageResponse.of(List.of(), null, 0, false, null));

            // when
            basicMessageService.findByChannelIdOrderByCreatedAtDesc(channelId, null, pageable);

            // then
            verify(messageRepository).findFirstPageByChannelId(eq(channelId), any());
            verify(messageRepository, never()).findNextPageByChannelId(any(), any(), any());
        }

        @Test
        @DisplayName("성공: 메시지 조회 (다건, cursor)")
        void findCursor_whenFind_thenFindNextPage() {
            Instant cursor = Instant.now();
            Pageable pageable = PageRequest.of(0, 10);
            Slice<Message> slice = new SliceImpl<>(List.of(), pageable, false);

            given(messageRepository.findNextPageByChannelId(eq(channelId), eq(cursor), any())).willReturn(slice);
            given(pageResponseMapper.fromSlice(any(), any(), anyBoolean())).willReturn(PageResponse.of(List.of(), null, 0, false, null));

            basicMessageService.findByChannelIdOrderByCreatedAtDesc(channelId, cursor, pageable);

            verify(messageRepository).findNextPageByChannelId(eq(channelId), eq(cursor), any());
            verify(messageRepository, never()).findFirstPageByChannelId(any(), any());
        }
    }

    @Nested
    @DisplayName("Update Message")
    class Update {
        @Test
        @DisplayName("성공: 메시지 수정")
        void givenUpdateRequest_whenUpdate_thenMessageUpdated() {
            // given
            Message message = new Message(channel, user, "message", List.of());
            given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

            MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("updatedMessage");

            MessageDto messageDto = new MessageDto(
                    messageId,
                    channelId,
                    userDto,
                    List.of(),
                    "updatedMessage",
                    Instant.MIN,
                    Instant.MIN
            );

            given(messageMapper.toDto(message)).willReturn(messageDto);

            // when
            MessageDto res = basicMessageService.update(messageId, messageUpdateRequest);

            // then
            then(messageRepository).should().findById(messageId);
            then(messageMapper).should().toDto(message);

            assertThat(res).isEqualTo(messageDto);
            assertThat(res.channelId()).isEqualTo(channelId);
            assertThat(res.author()).isEqualTo(messageDto.author());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID")
        void givenNonExistingId_whenUpdate_thenThrowsException() {
            // given
            MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("updatedMessage");

            given(messageRepository.findById(messageId)).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicMessageService.update(messageId, messageUpdateRequest))
                    .isInstanceOf(MessageNotFoundException.class);

            // then
            then(messageRepository).should().findById(messageId);
            then(messageMapper).should(never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Delete Message")
    class Delete {
        @Test
        @DisplayName("성공: 메시지 삭제")
        void givenDeleteId_whenDelete_thenMessageDeleted() {
            // given
            BinaryContent binaryContent1 = new BinaryContent("file1", 3L, "jpg");
            BinaryContent binaryContent2 = new BinaryContent("file2", 4L, "jpg");

            Message message = new Message(channel, user, "message", List.of(binaryContent1, binaryContent2));
            given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

            // when
            basicMessageService.delete(messageId);

            // then
            then(messageRepository).should().findById(messageId);
            then(binaryContentRepository).should().deleteAll(message.getAttachments());
            then(messageRepository).should().delete(message);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID")
        void givenNonExistingId_whenDelete_thenThrowsException() {
            // given
            given(messageRepository.findById(messageId)).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicMessageService.delete(messageId))
                    .isInstanceOf(MessageNotFoundException.class);

            // then
            then(messageRepository).should().findById(messageId);
            then(binaryContentRepository).should(never()).deleteAll(any());
            then(messageRepository).should(never()).delete(any());
        }
    }
}