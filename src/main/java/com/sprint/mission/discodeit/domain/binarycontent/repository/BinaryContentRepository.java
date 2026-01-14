package com.sprint.mission.discodeit.domain.binarycontent.repository;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID>
{
    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAll();
    List<BinaryContent> findAllByIds(List<UUID> ids);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
