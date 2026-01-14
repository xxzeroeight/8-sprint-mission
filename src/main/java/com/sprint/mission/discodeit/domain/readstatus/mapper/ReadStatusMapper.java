package com.sprint.mission.discodeit.domain.readstatus.mapper;

import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.dto.domain.ReadStatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper
{
    ReadStatusDto toDto(ReadStatus readStatus);
}
