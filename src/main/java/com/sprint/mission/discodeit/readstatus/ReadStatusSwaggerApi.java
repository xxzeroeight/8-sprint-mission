package com.sprint.mission.discodeit.readstatus;

import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readstatus.dto.response.ReadStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "ReadStatus API")
public interface ReadStatusSwaggerApi
{
    // createReadStatus
    @Operation(summary = "ReadStatus 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "ReadStatus가 성공적으로 생성됨.",
                    content = @Content(schema = @Schema(implementation = ReadStatusResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
                    content = @Content(examples = {
                            @ExampleObject(name = "channel을 찾을 수 없음.", value = "channel을 찾을 수 없음."),
                            @ExampleObject(name = "user를 찾을 수 없음.", value = "user를 찾을 수 없음."),
                    })
            ),
            @ApiResponse(
                    responseCode = "400", description = "ReadStatus가 이미 존재함.",
                    content = @Content(examples = @ExampleObject(value = "ReadStatus가 이미 존재함."))
            )
    })
    ResponseEntity<ReadStatusResponse> createReadStatus(
            @Parameter(description = "ReadStatus 생성 정보") ReadStatusCreateRequest readStatusCreateRequest
    );

    // updateReadStatus
    @Operation(summary = "ReadStatus 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "ReadStatus가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = ReadStatusResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "ReadStatus를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "ReadStatus with id {readStatusId} not found"))
            )
    })
    ResponseEntity<ReadStatusResponse> updateReadStatus(
            @Parameter(description = "수정할 ReadStatus의 id") UUID readStatusId,
            @Parameter(description = "수정할 ReadStatus의 정보") ReadStatusUpdateRequest readStatusUpdateRequest
    );

    // getReadStatuses
    @Operation(summary = "user의 ReadStatus 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "ReadStatus 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatusResponse.class)))
            )
    })
    ResponseEntity<List<ReadStatusResponse>> getReadStatuses(
            @Parameter(description = "조회할 user의 id") UUID userId
    );
}
