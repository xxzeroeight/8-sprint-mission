package com.sprint.mission.discodeit.domain.binarycontent.storage;


import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class AWSS3Test
{
    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private Dotenv dotenv;

    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String region;

    @BeforeEach
    void setUp() {
        dotenv = Dotenv.configure().ignoreIfMissing().load();

        accessKey = dotenv.get("AWS_ACCESS_KEY");
        secretKey = dotenv.get("AWS_SECRET_KEY");
        bucketName = dotenv.get("AWS_S3_BUCKET");
        region = dotenv.get("AWS_S3_REGION");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        Region awsRegion = Region.of(region);

        s3Client = S3Client.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        s3Presigner = S3Presigner.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Nested
    @DisplayName("Test S3")
    class s3Tests {
        @Test
        @DisplayName("성공: S3에 이미지 파일을 업로드한다.")
        void uploadTest() {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("images/profile/profile.png")
                    .contentType("image/png")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes("hello".getBytes()));

            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key("images/profile/profile.png")
                    .build();

            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);

            assertThat(headObjectResponse.contentType()).isEqualTo("image/png");
        }

        @Test
        @DisplayName("성공: S3에서 이미지 파일을 다운로드한다.")
        void downloadTest() {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key("images/profile/profile.png")
                    .build();

            ResponseBytes<GetObjectResponse> object = s3Client.getObjectAsBytes(getObjectRequest);
            byte[] byteArray = object.asByteArray();

            assertThat(new String(byteArray)).isEqualTo("hello");
        }

        @Test
        @DisplayName("성공: S3에 임시적으로 접근한다.")
        void presignTest() {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key("images/profile/profile.png")
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();

            URL url = s3Presigner.presignGetObject(getObjectPresignRequest).url();

            assertThat(url.toString()).contains("images/profile/profile.png");
        }
    }
}
