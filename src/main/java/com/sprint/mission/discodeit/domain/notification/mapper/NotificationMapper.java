package com.sprint.mission.discodeit.domain.notification.mapper;

import com.sprint.mission.discodeit.domain.notification.dto.entity.NotificationDto;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper
{
    NotificationDto toDto(Notification notification);
    List<NotificationDto> toDtoList(List<Notification> notifications);
}
