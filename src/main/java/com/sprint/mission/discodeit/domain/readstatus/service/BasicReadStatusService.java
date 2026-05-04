package com.sprint.mission.discodeit.domain.readstatus.service;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.domain.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.dto.domain.ReadStatusDto;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.readstatus.exception.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.domain.readstatus.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.domain.readstatus.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService
{
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    @Override
    public ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest) {
        User author = userRepository.findById(readStatusCreateRequest.userId())
                .orElseThrow(() -> new UserNotFoundException(readStatusCreateRequest.userId()));

        Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(readStatusCreateRequest.channelId()));

        if (readStatusRepository.findAllByUserId(readStatusCreateRequest.userId()).stream()
                .anyMatch(status -> status.getChannel().getId().equals(readStatusCreateRequest.channelId()))) {
            throw new ReadStatusAlreadyExistsException(readStatusCreateRequest.channelId(), readStatusCreateRequest.userId());
        }

        boolean notificationEnabled = channel.getType() == ChannelType.PRIVATE;

        ReadStatus readStatus = new ReadStatus(author, channel, readStatusCreateRequest.lastReadAt(), notificationEnabled);
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(savedReadStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .map(readStatus -> readStatusMapper.toDto(readStatus))
                .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> readStatusMapper.toDto(readStatus))
                .toList();
    }

    @Transactional
    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));

        readStatus.update(readStatusUpdateRequest.newLastReadAt(), readStatusUpdateRequest.newNotificationEnabled());
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(savedReadStatus);
    }

    @Transactional
    @Override
    public void delete(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                        .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));

        readStatusRepository.delete(readStatus);
    }
}
