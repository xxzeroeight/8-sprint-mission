package com.sprint.mission.discodeit.domain.binarycontent.service;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService
{
    BinaryContentDto create(BinaryContentCreateRequest binaryContentCreateRequest);
    BinaryContentDto find(UUID binaryContentId);
    List<BinaryContentDto> findAllByIds(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);
}
