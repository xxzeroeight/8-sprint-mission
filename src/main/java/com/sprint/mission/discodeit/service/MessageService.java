package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequest;

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
