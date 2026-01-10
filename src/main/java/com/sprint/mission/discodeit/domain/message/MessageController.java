package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageResponse;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
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
public class MessageController implements MessageSwaggerApi
{
    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> createMessage(@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
                                                         @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) throws IOException
    {
        List<BinaryContentCreateRequest> binaryContentCreateRequest = new ArrayList<>();
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                binaryContentCreateRequest.add(new BinaryContentCreateRequest(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                ));
            }
        }

        MessageDto message = messageService.create(messageCreateRequest, binaryContentCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.from(message));
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAllMessages(@RequestParam("channelId") UUID channelId)
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
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId)
    {
        messageService.delete(messageId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
