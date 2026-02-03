package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageResponse;
import com.sprint.mission.discodeit.domain.message.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> createMessage(@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
                                                         @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) throws IOException
    {
        log.debug("메시지 생성 시작(정보): authorId={}, channelId={}, content={}", messageCreateRequest.authorId(), messageCreateRequest.channelId(), messageCreateRequest.content());

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

        log.info("메시지 생성 완료: messageId={}", message.id());

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

        log.debug("메시지 조회(다건) 완료: count={}", messages.size());

        return ResponseEntity.status(HttpStatus.OK)
                .body(toResponsePage(messages));
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponse> updateMessage(@PathVariable UUID messageId,
                                                         @RequestBody MessageUpdateRequest messageUpdateRequest)
    {
        log.debug("메시지 정보 수정 시작: messageId={}, content={}", messageId, messageUpdateRequest.updateContent());

        MessageDto message = messageService.update(messageId, messageUpdateRequest);

        log.info("메시지 정보 수정 완료: messageId={}", message.id());

        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.from(message));
    }

    @DeleteMapping("{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId)
    {
        log.debug("메시지 삭제 시작: messageId={}", messageId);

        messageService.delete(messageId);

        log.info("메시지 삭제 완료: messageId={}", messageId);

        return ResponseEntity.status(HttpStatus.OK).build();
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
