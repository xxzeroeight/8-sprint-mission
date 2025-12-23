package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
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

        return toDto(savedReadStatus);
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .map(readStatus -> toDto(readStatus))
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> toDto(readStatus))
                .toList();
    }

    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));

        readStatus.update(readStatusUpdateRequest.updateLastReadAt());
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return toDto(savedReadStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw ReadStatusNotFoundException.byId(readStatusId);
        }

        readStatusRepository.deleteById(readStatusId);
    }

    private ReadStatusDto toDto(ReadStatus readStatus) {
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt(),
                readStatus.getLastReadAt()
        );
    }
}
