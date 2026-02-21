package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("S3BinaryContentStorage Unit Test")
class S3BinaryContentStorageTest
{
    @Mock S3Client s3Client;
    @Mock S3Presigner s3Presigner;
    @Mock ResponseInputStream<GetObjectResponse> responseInputStream;
    @Mock PresignedGetObjectRequest presignedGetObjectRequest;

    private S3BinaryContentStorage s3BinaryContentStorage;

    @BeforeEach
    void setUp() {
        s3BinaryContentStorage = new S3BinaryContentStorage(
                "test-bucket",
                "ap-northeast-2",
                "test-access-key",
                "test-secret-key"
        );

        ReflectionTestUtils.setField(s3BinaryContentStorage, "s3Client", s3Client);
        ReflectionTestUtils.setField(s3BinaryContentStorage, "s3Presigner", s3Presigner);
        ReflectionTestUtils.setField(s3BinaryContentStorage, "presignedUrlExpirationDuration", 600);
    }

    @Test
    @DisplayName("성공: S3에 파일을 저장한다.")
    void givenFile_whenSave_thenFileSaved() {
        // given
        UUID uuid = UUID.randomUUID();
        byte[] content = "hello".getBytes();

        given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).willReturn(PutObjectResponse.builder().build());

        // when
        UUID res = s3BinaryContentStorage.save(uuid, content);

        // then
        assertThat(res).isEqualTo(uuid);
        then(s3Client).should().putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    @DisplayName("성공: S3 객체의 InputStream을 반환한다.")
    void given_when_then() {
        // given
        UUID uuid = UUID.randomUUID();
        InputStream mockStream = mock(InputStream.class);

        given(s3Client.getObject(any(GetObjectRequest.class))).willReturn(responseInputStream);

        // when
        InputStream res = s3BinaryContentStorage.openStream(uuid);

        // then
        assertThat(res).isNotNull();
        then(s3Client).should().getObject(any(GetObjectRequest.class));
    }
    
    @Test
    @DisplayName("성공: 다운로드에 성공한다. (302 리다이렉트 응답)")
    void givenExistingId_whenDownload_then302Redirection() throws MalformedURLException {
        // given
        UUID uuid = UUID.randomUUID();
        BinaryContentDto binaryContentDto = new BinaryContentDto(uuid, "profile.png", "image/png", 5L);

        String presignedUrl = "https://test-bucket.s3.amazonaws.com/" + uuid + "?X-Amz-Signature=qwer";

        given(presignedGetObjectRequest.url()).willReturn(new URL(presignedUrl));
        given(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).willReturn(presignedGetObjectRequest);
        
        // when
        ResponseEntity<Void> res = s3BinaryContentStorage.download(binaryContentDto);
        
        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(res.getHeaders().getLocation()).isEqualTo(URI.create(presignedUrl));
    }
}