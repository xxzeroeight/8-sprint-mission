package com.sprint.mission.discodeit.domain.binarycontent;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentDownloadResponse;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BinaryContentResponse.from(binaryContent));
    }

    @GetMapping
    public ResponseEntity<List<BinaryContentResponse>> getBinaryContents(@RequestParam("binaryContentIds") List<UUID> ids)
    {
        List<BinaryContentDto> binaryContents = binaryContentService.findAllByIds(ids);

        List<BinaryContentResponse> binaryContentResponses = binaryContents.stream()
                .map(BinaryContentResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(binaryContentResponses);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID binaryContentId)
    {
        BinaryContentDownloadResponse file = binaryContentService.download(binaryContentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .contentLength(file.size())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.fileName() + "\"")
                .body(file.resource());
    }
}
