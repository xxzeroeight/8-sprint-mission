package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService
{
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentDto create(BinaryContentCreateRequest binaryContentCreateRequest) {
        BinaryContent binaryContent = new BinaryContent(
                binaryContentCreateRequest.fileName(),
                (long) binaryContentCreateRequest.bytes().length,
                binaryContentCreateRequest.contentType(),
                binaryContentCreateRequest.bytes()
        );

        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        return toDto(savedBinaryContent);
    }

    @Override
    public BinaryContentDto find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .map(binaryContent -> toDto(binaryContent))
                .orElseThrow(() -> BinaryContentNotFoundException.byId(binaryContentId));
    }

    @Override
    public List<BinaryContentDto> findAllByIds(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIds(binaryContentIds).stream()
                .map(binaryContent -> toDto(binaryContent))
                .toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw BinaryContentNotFoundException.byId(binaryContentId);
        }

        binaryContentRepository.deleteById(binaryContentId);
    }

    private BinaryContentDto toDto(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getSize(),
                binaryContent.getBytes(),
                binaryContent.getCreatedAt()
        );
    }
}
