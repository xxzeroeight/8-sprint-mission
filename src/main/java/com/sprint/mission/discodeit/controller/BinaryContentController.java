package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binary-contents")
public class BinaryContentController
{
    private final BinaryContentService binaryContentService;

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
}
