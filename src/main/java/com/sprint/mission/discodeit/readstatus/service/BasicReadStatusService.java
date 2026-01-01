package com.sprint.mission.discodeit.readstatus.service;

import com.sprint.mission.discodeit.readstatus.dto.domain.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.readstatus.exception.DuplicateReadStatusException;
import com.sprint.mission.discodeit.readstatus.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService
{
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest) {
        if (!userRepository.existsById(readStatusCreateRequest.userId())) {
            throw UserNotFoundException.byId(readStatusCreateRequest.userId());
        }

        if (!channelRepository.existsById(readStatusCreateRequest.channelId())) {
            throw ChannelNotFoundException.byId(readStatusCreateRequest.channelId());
        }

        if (readStatusRepository.findAllByUserId(readStatusCreateRequest.userId()).stream()
                .anyMatch(status -> status.getChannelId().equals(readStatusCreateRequest.channelId()))) {
            throw new DuplicateReadStatusException("이미 해당 채널의 읽음 상태가 존재합니다.");
        }

        Instant lastReadAt = readStatusCreateRequest.lastReadAt() != null ? readStatusCreateRequest.lastReadAt() : Instant.now();

        ReadStatus readStatus = new ReadStatus(readStatusCreateRequest.userId(), readStatusCreateRequest.channelId(), lastReadAt);
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return ReadStatusDto.from(savedReadStatus);
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .map(readStatus -> ReadStatusDto.from(readStatus))
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> ReadStatusDto.from(readStatus))
                .toList();
    }

    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));

        readStatus.update(readStatusUpdateRequest.updateLastReadAt());
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return ReadStatusDto.from(savedReadStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw ReadStatusNotFoundException.byId(readStatusId);
        }

        readStatusRepository.deleteById(readStatusId);
    }
}
