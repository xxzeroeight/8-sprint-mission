package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.message.dto.domain.MessageDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateRequest;

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
