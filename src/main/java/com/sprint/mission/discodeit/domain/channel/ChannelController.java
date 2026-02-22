package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.domain.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelSwaggerApi
{
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelResponse> createPublicChannel(@Valid @RequestBody PublicChannelCreateRequest publicChannelCreateRequest)
    {
        log.debug("채널 생성 시작(공개): name={}, description=={}", publicChannelCreateRequest.name(), publicChannelCreateRequest.description());

        ChannelDto channel = channelService.create(publicChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChannelResponse.from(channel));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResponse> createPrivateChannel(@Valid @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest)
    {
        log.debug("채널 생성 시작(비공개): paticipants={}", privateChannelCreateRequest.participantIds());

        ChannelDto channel = channelService.create(privateChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChannelResponse.from(channel));
    }

    @GetMapping
    public ResponseEntity<List<ChannelResponse>> getAllChannels(@RequestParam("userId") UUID userId)
    {
        log.debug("사용자 참여 채널 조회 시작:  userId={}", userId);

        List<ChannelDto> channels = channelService.findAllByUserId(userId);

        List<ChannelResponse> channelResponses = channels.stream()
                .map(ChannelResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(channelResponses);
    }

    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> updatePublicChannel(@PathVariable UUID channelId,
                                                               @Valid @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest)
    {
        log.debug("채널 정보 수정 시작: channelId={}, name={}, description={}", channelId, publicChannelUpdateRequest.updateName(), publicChannelUpdateRequest.updateDescription());

        ChannelDto channel = channelService.update(channelId, publicChannelUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ChannelResponse.from(channel));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId)
    {
        log.debug("채널 삭제 시작: channelId={}", channelId);

        channelService.delete(channelId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
