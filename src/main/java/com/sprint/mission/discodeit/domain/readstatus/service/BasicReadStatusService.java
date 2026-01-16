package com.sprint.mission.discodeit.domain.readstatus.service;

import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.dto.domain.ReadStatusDto;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.domain.readstatus.exception.DuplicateReadStatusException;
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
                .orElseThrow(() -> UserNotFoundException.byId(readStatusCreateRequest.userId()));

        Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
                .orElseThrow(() -> ChannelNotFoundException.byId(readStatusCreateRequest.channelId()));

        if (readStatusRepository.findAllByUserId(readStatusCreateRequest.userId()).stream()
                .anyMatch(status -> status.getChannel().getId().equals(readStatusCreateRequest.channelId()))) {
            throw new DuplicateReadStatusException("이미 해당 채널의 읽음 상태가 존재합니다.");
        }

        ReadStatus readStatus = new ReadStatus(author, channel, readStatusCreateRequest.lastReadAt());

        return readStatusMapper.toDto(readStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .map(readStatus -> readStatusMapper.toDto(readStatus))
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));
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
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));

        readStatus.update(readStatusUpdateRequest.newLastReadAt());
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(savedReadStatus);
    }

    @Transactional
    @Override
    public void delete(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                        .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));

        readStatusRepository.delete(readStatus);
    }
}
