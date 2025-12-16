package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest) {
        if (!userRepository.existsById(readStatusCreateRequest.userId())) {
            throw UserNotFoundException.byId(readStatusCreateRequest.userId());
        }

        if (!channelRepository.existsById(readStatusCreateRequest.channleId())) {
            throw ChannelNotFoundException.byId(readStatusCreateRequest.channleId());
        }

        ReadStatus readStatus = new ReadStatus(readStatusCreateRequest.userId(), readStatusCreateRequest.channleId(), readStatusCreateRequest.lastReadAt());
        readStatusRepository.save(readStatus);

        return readStatus;
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .toList();
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));

        readStatus.update(readStatusUpdateRequest.updateLastReadAt());

        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw ReadStatusNotFoundException.byId(readStatusId);
        }

        readStatusRepository.deleteById(readStatusId);
    }
}
