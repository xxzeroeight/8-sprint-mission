package com.sprint.mission.discodeit.domain.binarycontent.service;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentDownloadResponse;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BinaryContentService Unit Test")
class BasicBinaryContentServiceTest
{
    @InjectMocks private BasicBinaryContentService basicBinaryContentService;

    @Mock private BinaryContentRepository binaryContentRepository;
    @Mock private BinaryContentStorage binaryContentStorage;
    @Mock private BinaryContentMapper binaryContentMapper;

    private UUID binaryContentId;

    private BinaryContent binaryContent;
    private BinaryContentDto binaryContentDto;

    @BeforeEach
    void setUp() {
        binaryContentId = UUID.randomUUID();
        binaryContent = new BinaryContent("file.jpg", 5L, "image/jpeg");
        binaryContentDto = new BinaryContentDto(binaryContent.getId(), "file.jpg", "image/jpeg", 5L);
    }

    @Nested
    @DisplayName("Create BinaryContent")
    class Create {
        @Test
        @DisplayName("성공: 바이너리 컨텐츠 생성")
        void givenCreateRequest_whenCreate_thenBinaryContentCreated() {
            // given
            BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest("file.jpg", "image/jpeg", "hello".getBytes());

            given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);
            given(binaryContentMapper.toDto(binaryContent)).willReturn(binaryContentDto);

            // when
            BinaryContentDto res = basicBinaryContentService.create(binaryContentCreateRequest);

            // then
            then(binaryContentRepository).should().save(any(BinaryContent.class));
            then(binaryContentStorage).should().save(binaryContent.getId(), "hello".getBytes());
            then(binaryContentMapper).should().toDto(binaryContent);

            assertThat(res).isEqualTo(binaryContentDto);
        }
    }

    @Nested
    @DisplayName("Delete BinaryContent")
    class Delete {
        @Test
        @DisplayName("성공: 바이너리 컨텐츠 삭제")
        void givenBinartContentId_whenDelete_thenBinaryContentDeleted() {
            // given
            given(binaryContentRepository.findById(any(UUID.class))).willReturn(Optional.of(binaryContent));

            // when
            basicBinaryContentService.delete(binaryContentId);

            // then
            then(binaryContentRepository).should().findById(any(UUID.class));
            then(binaryContentRepository).should().delete(binaryContent);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID")
        void givenNonExistingId_whenDelete_thenThrowsException() {
            // given
            given(binaryContentRepository.findById(any(UUID.class))).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> basicBinaryContentService.delete(UUID.randomUUID()))
                    .isInstanceOf(BinaryContentNotFoundException.class);

            // then
            then(binaryContentRepository).should().findById(any(UUID.class));
            then(binaryContentRepository).should(never()).delete(binaryContent);
        }
    }

    @Test
    @DisplayName("성공: 바이너리 컨텐츠 다운로드")
    void givenBinaryContentId_whenDownload_thenBinaryContentDownloaded() {
        // given
        InputStream inputStream = new ByteArrayInputStream("hello".getBytes());

        given(binaryContentRepository.findById(binaryContent.getId())).willReturn(Optional.of(binaryContent));
        given(binaryContentMapper.toDto(binaryContent)).willReturn(binaryContentDto);
        given(binaryContentStorage.openStream(binaryContent.getId())).willReturn(inputStream);

        // when
        BinaryContentDownloadResponse res = basicBinaryContentService.download(binaryContent.getId());

        // then
        then(binaryContentRepository).should().findById(binaryContent.getId());
        then(binaryContentMapper).should().toDto(binaryContent);
        then(binaryContentStorage).should().openStream(eq(binaryContent.getId()));

        assertThat(res.fileName()).isEqualTo(binaryContent.getFileName());
        assertThat(res.resource()).isNotNull();
    }
}