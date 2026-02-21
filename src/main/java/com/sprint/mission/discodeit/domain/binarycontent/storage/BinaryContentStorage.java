package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage
{
    UUID save(UUID id, byte[] bytes);
    InputStream openStream(UUID id);
    ResponseEntity<?> download(BinaryContentDto binaryContentDto);
}
