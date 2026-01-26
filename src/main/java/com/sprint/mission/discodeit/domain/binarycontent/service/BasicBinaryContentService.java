package com.sprint.mission.discodeit.domain.binarycontent.service;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentDownloadResponse;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService
{
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public BinaryContentDto create(BinaryContentCreateRequest binaryContentCreateRequest) {
        BinaryContent binaryContent = new BinaryContent(
                binaryContentCreateRequest.fileName(),
                (long) binaryContentCreateRequest.bytes().length,
                binaryContentCreateRequest.contentType()
        );

        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        binaryContentStorage.save(savedBinaryContent.getId(), binaryContentCreateRequest.bytes());

        return binaryContentMapper.toDto(savedBinaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentDto find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .map(binaryContent -> binaryContentMapper.toDto(binaryContent))
                .orElseThrow(() -> BinaryContentNotFoundException.byId(binaryContentId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentDto> findAllByIds(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds).stream()
                .map(binaryContent -> binaryContentMapper.toDto(binaryContent))
                .toList();
    }

    @Transactional
    @Override
    public void delete(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> BinaryContentNotFoundException.byId(binaryContentId));

        binaryContentRepository.delete(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentDownloadResponse download(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> BinaryContentNotFoundException.byId(binaryContentId));

        BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);
        InputStream inputStream = binaryContentStorage.openStream(binaryContentDto.id());

        Resource resource = new InputStreamResource(inputStream);

        return BinaryContentDownloadResponse.from(resource, binaryContentDto);
    }
}
