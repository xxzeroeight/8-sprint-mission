package com.sprint.mission.discodeit.domain.binarycontent;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentDownloadResponse;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentSwaggerApi
{
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResponse> getBinaryContent(@PathVariable UUID binaryContentId)
    {
        log.debug("바이너리 컨텐츠 조회(단건) 시작: binaryContentId={}", binaryContentId);

        BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);

        log.debug("바이너리 컨텐츠 조회(단건) 완료: binaryContentId={}", binaryContent.id());

        return ResponseEntity.status(HttpStatus.OK)
                .body(BinaryContentResponse.from(binaryContent));
    }

    @GetMapping
    public ResponseEntity<List<BinaryContentResponse>> getBinaryContents(@RequestParam("binaryContentIds") List<UUID> ids)
    {
        log.debug("바이너리 컨텐츠 조회(다건) 시작: count={}", ids.size());

        List<BinaryContentDto> binaryContents = binaryContentService.findAllByIds(ids);

        List<BinaryContentResponse> binaryContentResponses = binaryContents.stream()
                .map(BinaryContentResponse::from)
                .toList();

        log.debug("바이너리 컨텐츠 조회(다건) 완료: count={}", binaryContentResponses.size());

        return ResponseEntity.status(HttpStatus.OK)
                .body(binaryContentResponses);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID binaryContentId)
    {
        log.debug("바이너리 컨텐츠 다운로드 시작: binaryContentId={}", binaryContentId);

        BinaryContentDownloadResponse file = binaryContentService.download(binaryContentId);

        log.debug("바이너리 컨텐츠 다운로드 완료: binaryContentId={}", binaryContentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .contentLength(file.size())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.fileName() + "\"")
                .body(file.resource());
    }
}
