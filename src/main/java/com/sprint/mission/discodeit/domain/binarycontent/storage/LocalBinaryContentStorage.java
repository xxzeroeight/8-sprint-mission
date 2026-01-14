package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class LocalBinaryContentStorage implements BinaryContentStorage
{
    private final Path root;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path:./uploads}") Path root) {
        this.root = root.toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("초기화하지 못했습니다."); // 나중에 예외 처리
        }
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        try {
            Path filePath = resolvePath(id);
            Files.write(filePath, bytes);
            return id;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다: " + id);
        }
    }

    @Override
    public InputStream get(UUID id) {
        try {
            Path filePath = resolvePath(id);
            if (!Files.exists(filePath)) {
                throw new RuntimeException("파일을 찾을 수 없습니다: " + id);
            }

            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일을 가져오는 데 실패했습니다: " + id);
        }
    }

    // 파일 경로 얻음. -> Spring Resource로 변환(스트리밍) -> 유효성 검사. -> 스트리밍으로 전송.
    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try {
            InputStream inputStream = get(binaryContentDto.id());

            Resource resource = new InputStreamResource(inputStream);

            // inline: default, 브라우저 내부
            // attachment; filename: '다른 이름으로 저장'
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
                    .contentLength(binaryContentDto.size())
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("파일 다운로드에 실패했습니다: " + binaryContentDto.id());
        }
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString()).normalize();
    }
}
