package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelResponse;
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

@Tag(name = "Channel", description = "Channel API")
public interface ChannelSwaggerApi
{
    // createPublicChannel
    @Operation(summary = "Channel 생성(Publuc)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = ChannelResponse.class))
            )
    })
    ResponseEntity<ChannelResponse> createPublicChannel(
            @Parameter(description = "Public Channel 생성 정보") PublicChannelCreateRequest publicChannelCreateRequest
    );

    // createPrivateChannel
    @Operation(summary = "Channel 생성(Private")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = ChannelResponse.class))
            )
    })
    ResponseEntity<ChannelResponse> createPrivateChannel(
            @Parameter(description = "Private Channel 생성 정보") PrivateChannelCreateRequest privateChannelCreateRequest
    );

    // getAllChannels
    @Operation(summary = "User가 참여 중인 Channel 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Channel 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelResponse.class)))
            )
    })
    ResponseEntity<List<ChannelResponse>> getAllChannels(
            @Parameter(description = "조회할 User ID") UUID userId
    );

    // updatePublicChannel
    @Operation(summary = "Channel 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Channel를 성공적으로 수정함.",
                    content = @Content(schema = @Schema(implementation = ChannelResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Channel을 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "Channel을 찾을 수 없음."))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Private Channel은 수정할 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "Private Channel은 수정할 수 없음."))
            )
    })
    ResponseEntity<ChannelResponse> updatePublicChannel(
            @Parameter(description = "수정할 channel의 id") UUID channelId,
            @Parameter(description = "수정할 Channel의 내용") PublicChannelUpdateRequest publicChannelUpdateRequest
    );

    // deleteChannel
    @Operation(summary = "Channel 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "Channel이 성공적으로 삭제됨."
            ),
            @ApiResponse(
                    responseCode = "404", description = "Channel을 찾을 수 없음.",
                    content = @Content(examples = @ExampleObject(value = "Channel을 찾을 수 없음."))
            )
    })
    ResponseEntity<Void> deleteChannel(
            @Parameter(description = "삭제할 channel의 id") UUID channelId
    );
}
