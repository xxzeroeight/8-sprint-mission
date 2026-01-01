package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.dto.request.BinaryContentCreateRequest;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService
{
    BinaryContentDto create(BinaryContentCreateRequest binaryContentCreateRequest);
    BinaryContentDto find(UUID binaryContentId);
    List<BinaryContentDto> findAllByIds(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);
}
