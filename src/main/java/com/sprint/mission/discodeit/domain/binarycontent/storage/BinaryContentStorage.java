package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage
{
    UUID put(UUID id, byte[] bytes);
    InputStream get(UUID id);
    ResponseEntity<?> download(BinaryContentDto binaryContentDto);
}
