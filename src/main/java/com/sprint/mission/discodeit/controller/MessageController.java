package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.entity.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController
{
    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> cretaeMessage(@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
                                                         @RequestPart(value = "messageImages", required = false) MultipartFile messageImages) throws IOException
    {
        List<BinaryContentCreateRequest> binaryContentCreateRequest = new ArrayList<>();
        if (messageImages != null) {
            binaryContentCreateRequest = List.of(new BinaryContentCreateRequest(
                    messageImages.getOriginalFilename(),
                    messageImages.getContentType(),
                    messageImages.getBytes()
            ));
        }

        MessageDto message = messageService.create(messageCreateRequest, binaryContentCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.from(message));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByChannelId(@PathVariable UUID channelId)
    {
        List<MessageDto> messages = messageService.findAllByChannelId(channelId);

        List<MessageResponse> messagesResponses = messages.stream()
                .map(MessageResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(messagesResponses);
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponse> updateMessage(@PathVariable UUID messageId,
                                                         @RequestBody MessageUpdateRequest messageUpdateRequest)
    {
        MessageDto message = messageService.update(messageId, messageUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.from(message));
    }

    @DeleteMapping("{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId)
    {
        messageService.delete(messageId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
