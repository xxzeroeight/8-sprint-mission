package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.ChannelDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.service.ChannelService;
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

    @PostMapping("/create/public")
    public ResponseEntity<ChannelResponse> createPublicChannel(@RequestBody PublicChannelCreateRequest publicChannelCreateRequest)
    {
        ChannelDto channel = channelService.create(publicChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChannelResponse.from(channel));
    }

    @PostMapping("/create/private")
    public ResponseEntity<ChannelResponse> createPrivateChannel(@RequestBody PrivateChannelCreateRequest privateChannelCreateRequest)
    {
        ChannelDto channel = channelService.create(privateChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChannelResponse.from(channel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ChannelResponse>> getUserChannels(@PathVariable UUID id)
    {
        List<ChannelDto> channels = channelService.findAllByUserId(id);

        List<ChannelResponse> channelResponses = channels.stream()
                .map(ChannelResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(channelResponses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChannelResponse> updateChannel(@PathVariable UUID id,
                                                         @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest)
    {
        ChannelDto channel = channelService.update(id, publicChannelUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ChannelResponse.from(channel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id)
    {
        channelService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
