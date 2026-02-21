package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage
{
    private String bucketName;
    private String region;
    private String accessKey;
    private String secretKey;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @Value("${discodeit.storage.s3.presigned-url-expiration:600}")
    private int presignedUrlExpirationDuration;

    public S3BinaryContentStorage(@Value("${discodeit.storage.s3.bucket}") String bucketName,
                                  @Value("${discodeit.storage.s3.region}") String region,
                                  @Value("${discodeit.storage.s3.access-key}") String accessKey,
                                  @Value("${discodeit.storage.s3.secret-key}") String secretKey)
    {
        log.info("Creating S3BinaryContentStorage");
        this.bucketName = bucketName;
        this.region = region;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void init() {
        this.s3Client = getS3Client();
        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build();
    }

    @Override
    public UUID save(UUID id, byte[] bytes) {
        log.debug("S3 파일 요청 시작: Id={}", id);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(id.toString())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

        log.info("S3 파일 요청 완료: Id={}", id);

        return id;
    }

    @Override
    public InputStream openStream(UUID id) {
        log.debug("S3 파일 조회 시작: Id={}", id);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(id.toString())
                .build();

        log.info("S3 파일 조회 완료: Id={}", id);

        return s3Client.getObject(getObjectRequest);
    }

    @Override
    public ResponseEntity<Void> download(BinaryContentDto binaryContentDto) {
        log.debug("S3 파일 다운로드 시작: Id={}", binaryContentDto.id());

        String url = generatePresignedUrl(binaryContentDto.id().toString(), binaryContentDto.contentType());

        log.info("S3 파일 다운로드 완료: Id={}", binaryContentDto.id());

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        String filename = extractFileNameWithExtension(key, contentType);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .responseContentType(contentType)
                .responseContentDisposition("attachment; filename=\"" + filename + "\"")
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(presignedUrlExpirationDuration))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
    }

    private S3Client getS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private String extractFileNameWithExtension(String filename, String contentType) {
        String name = filename.substring(filename.lastIndexOf("/") + 1);

        String extenstion = switch (contentType) {
            case "image/png" -> "png";
            case "image/gif" -> "gif";

            default -> "jpg";
        };

        return name + "." + extenstion;
    }
}
