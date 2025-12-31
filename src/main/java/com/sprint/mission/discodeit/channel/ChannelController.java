package com.sprint.mission.discodeit.channel;

import com.sprint.mission.discodeit.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController
{
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelResponse> createPublicChannel(@RequestBody PublicChannelCreateRequest publicChannelCreateRequest)
    {
        ChannelDto channel = channelService.create(publicChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChannelResponse.from(channel));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResponse> createPrivateChannel(@RequestBody PrivateChannelCreateRequest privateChannelCreateRequest)
    {
        ChannelDto channel = channelService.create(privateChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChannelResponse.from(channel));
    }

    @GetMapping
    public ResponseEntity<List<ChannelResponse>> getAllChannelsByUserId(@RequestParam("userId") UUID userId)
    {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);

        List<ChannelResponse> channelResponses = channels.stream()
                .map(ChannelResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(channelResponses);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> updatePublicChannel(@PathVariable UUID channelId,
                                                               @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest)
    {
        ChannelDto channel = channelService.update(channelId, publicChannelUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ChannelResponse.from(channel));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId)
    {
        channelService.delete(channelId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
