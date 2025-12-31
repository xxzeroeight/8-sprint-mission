package com.sprint.mission.discodeit.readstatus;

import com.sprint.mission.discodeit.readstatus.dto.domain.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readstatus.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController
{
    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusResponse> createReadStatus(@RequestBody ReadStatusCreateRequest readStatusCreateRequest)
    {
        ReadStatusDto readStatus = readStatusService.create(readStatusCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReadStatusResponse.from(readStatus));
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResponse> updateReadStatus(@PathVariable UUID readStatusId,
                                                               @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest)
    {
        ReadStatusDto readStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ReadStatusResponse.from(readStatus));
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusResponse>> getReadStatusByUserId(@RequestParam("userId") UUID userId)
    {
        List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);

        List<ReadStatusResponse> readStatusResponses = readStatuses.stream()
                .map(ReadStatusResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(readStatusResponses);
    }
}
