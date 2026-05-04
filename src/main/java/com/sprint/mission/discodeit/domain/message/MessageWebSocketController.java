package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController
{
    private final MessageService messageService;

    @MessageMapping("messages")
    public MessageDto sendMessage(@Payload MessageCreateRequest messageCreateRequest) {
        MessageDto createdMessage = messageService.create(messageCreateRequest, new ArrayList<>());
        return createdMessage;
    }
}
