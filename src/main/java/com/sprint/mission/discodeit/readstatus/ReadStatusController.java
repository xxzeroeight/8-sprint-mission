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
@RequestMapping("/api/read-status")
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

    @PatchMapping("/{id}")
    public ResponseEntity<ReadStatusResponse> updateReadStatus(@PathVariable UUID id,
                                                               @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest)
    {
        ReadStatusDto readStatus = readStatusService.update(id, readStatusUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ReadStatusResponse.from(readStatus));
    }

    @GetMapping("{id}")
    public ResponseEntity<List<ReadStatusResponse>> getReadStatusByUserId(@PathVariable UUID id)
    {
        List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(id);

        List<ReadStatusResponse> readStatusResponses = readStatuses.stream()
                .map(ReadStatusResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(readStatusResponses);
    }
}
