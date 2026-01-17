package com.sprint.mission.discodeit.domain.binarycontent.mapper;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.domain.BinaryContentDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper
{
    BinaryContentDto toDto(BinaryContent binaryContent);

    List<BinaryContentDto> toDtoList(List<BinaryContent> binaryContents);
}
