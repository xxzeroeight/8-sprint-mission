package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.channel.dto.domain.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService
{
    ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest);
    ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest);
    ChannelDto find(UUID channelId);
    List<ChannelDto> findAllByUserId(UUID userId);
    ChannelDto update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest);
    void delete(UUID channelId);
}
