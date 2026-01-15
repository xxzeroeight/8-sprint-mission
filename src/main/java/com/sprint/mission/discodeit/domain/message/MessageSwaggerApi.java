package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.PageResponse;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
public interface MessageSwaggerApi
{
    // createMessage
    @Operation(summary = "Message 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Message가 성공적으로 생성됨.",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "channel 또는 user를 찾을 수 없음.",
                    content = @Content(examples = {
                            @ExampleObject(name = "channel을 찾을 수 없음.", value = "channel을 찾을 수 없음."),
                            @ExampleObject(name = "user를 찾을 수 없음.", value = "user를 찾을 수 없음."),
                    })
            )
    })
    ResponseEntity<MessageResponse> createMessage(
            @Parameter(
                    description = "Message 생성 정보",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) MessageCreateRequest messageCreateRequest,
            @Parameter(
                    description = "Messsage 이미지(JSON)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) List<MultipartFile> attachments
    ) throws IOException;

    // getMessage
    @Operation(summary = "message 찾기(다건)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "message를 성공적으로 찾음.",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            )
    })
    ResponseEntity<PageResponse<MessageResponse>> getAllMessages(
            @Parameter(description = "조회할 channle의 id") UUID channelId,
            @Parameter(description = "페이지") int page
    );

    // updateMessage
    @Operation(summary = "Message 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "message를 성공적으로 수정함.",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "message를 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "message를 찾을 수 없음."))
            )
    })
    ResponseEntity<MessageResponse> updateMessage(
            @Parameter(description = "수정할 message의 id") UUID messageId,
            @Parameter(description = "수정할 message의 내용") MessageUpdateRequest messageUpdateRequest
    );

    // deleteMessage
    @Operation(summary = "Message 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "message가 성공적으로 삭제됨."),
            @ApiResponse(
                    responseCode = "404", description = "message를 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "message를 찾을 수 없음."))
            )
    })
    ResponseEntity<Void> deleteMessage(
            @Parameter(description = "삭제할 message의 id") UUID messageId
    );
}
