package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.event.S3UploadFailedEvent;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkServiceException;
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
    private final String bucketName;
    private final String region;
    private final String accessKey;
    private final String secretKey;

    private final ApplicationEventPublisher eventPublisher;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @Value("${discodeit.storage.s3.expiration-duration-in-minutes:60}")
    private int expirationDurationInMinutes;

    public S3BinaryContentStorage(@Value("${discodeit.storage.s3.bucket}") String bucketName,
                                  @Value("${discodeit.storage.s3.region}") String region,
                                  @Value("${discodeit.storage.s3.access-key}") String accessKey,
                                  @Value("${discodeit.storage.s3.secret-key}") String secretKey,
                                  ApplicationEventPublisher eventPublisher)
    {
        log.info("Creating S3BinaryContentStorage");
        this.bucketName = bucketName;
        this.region = region;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void init() {
        this.s3Client = getS3Client();
        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build();
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2, maxDelay = 10000))
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

    @Recover
    public UUID recover(Exception ex, UUID binaryContentId, byte[] bytes) {
        log.error("S3 업로드 실패: Id={}, Exception={}", binaryContentId, ex.getMessage());

        String requestID = (ex instanceof SdkServiceException sdkEx) ? sdkEx.requestId() : null;

        eventPublisher.publishEvent(
                new S3UploadFailedEvent(requestID, binaryContentId, ex.getMessage())
        );

        return null;
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
                .signatureDuration(Duration.ofMinutes(expirationDurationInMinutes))
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

        String extension = switch (contentType) {
            case "image/png" -> "png";
            case "image/gif" -> "gif";

            default -> "jpg";
        };

        return name + "." + extension;
    }
}
