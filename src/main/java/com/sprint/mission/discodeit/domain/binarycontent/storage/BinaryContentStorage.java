package com.sprint.mission.discodeit.domain.binarycontent.storage;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage
{
    UUID save(UUID id, byte[] bytes);
    InputStream openStream(UUID id);
}
