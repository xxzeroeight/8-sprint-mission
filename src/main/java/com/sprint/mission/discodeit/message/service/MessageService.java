package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService
{
    MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);
    MessageDto findById(UUID messageId);
    List<MessageDto> findAllByChannelId(UUID channelId);
    MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest);
    void delete(UUID messageId);
}
