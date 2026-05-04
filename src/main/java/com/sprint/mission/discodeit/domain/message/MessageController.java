package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageResponse;
import com.sprint.mission.discodeit.domain.message.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController implements MessageSwaggerApi
{
    private final MessageService messageService;

    @Timed("message.create.async")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
                                                         @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) throws IOException
    {
        log.debug("메시지 생성 시작(정보): authorId={}, channelId={}", messageCreateRequest.authorId(), messageCreateRequest.channelId());

        List<BinaryContentCreateRequest> binaryContentCreateRequest = new ArrayList<>();
        if (attachments != null) {
            log.debug("메시지 생성 시작(첨부파일): count={}", attachments.size());

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
    public ResponseEntity<PageResponse<MessageResponse>> getAllMessages(@RequestParam("channelId") UUID channelId,
                                                                        @RequestParam(required = false) Instant cursor,
                                                                        @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
    {
        log.debug("메시지 조회(다건) 시작: channelId={}, cursor={}, pageSize={}", channelId, cursor, pageable.getPageSize());

        PageResponse<MessageDto> messages = messageService.findByChannelIdOrderByCreatedAtDesc(channelId, cursor, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(toResponsePage(messages));
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponse> updateMessage(@PathVariable UUID messageId,
                                                         @Valid @RequestBody MessageUpdateRequest messageUpdateRequest)
    {
        log.debug("메시지 정보 수정 시작: messageId={}", messageId);

        MessageDto message = messageService.update(messageId, messageUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.from(message));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId)
    {
        log.debug("메시지 삭제 시작: messageId={}", messageId);

        messageService.delete(messageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private PageResponse<MessageResponse> toResponsePage(PageResponse<MessageDto> messageDtoPageResponse) {
        List<MessageResponse> responses = messageDtoPageResponse.content().stream()
                .map(MessageResponse::from)
                .toList();

        return PageResponse.of(
                responses,
                messageDtoPageResponse.nextCursor(),
                responses.size(),
                messageDtoPageResponse.hasNext(),
                null
        );
    }
}
