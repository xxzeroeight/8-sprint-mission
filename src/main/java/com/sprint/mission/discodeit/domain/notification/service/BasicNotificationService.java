package com.sprint.mission.discodeit.domain.notification.service;

import com.sprint.mission.discodeit.domain.notification.dto.entity.NotificationDto;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.exception.NotificationForBiddenException;
import com.sprint.mission.discodeit.domain.notification.exception.NotificationNotFoundException;
import com.sprint.mission.discodeit.domain.notification.mapper.NotificationMapper;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicNotificationService implements NotificationService
{
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public NotificationDto create(UUID receiverId, String title, String content) {
        Notification savedNotification = notificationRepository.save(new Notification(receiverId, title, content));

        return notificationMapper.toDto(savedNotification);
    }

    @Transactional(readOnly = true)
    @Override
    public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
        return notificationMapper.toDtoList(notificationRepository.findAllByReceiverId(receiverId));
    }

    @Transactional
    @Override
    public void delete(UUID notificationId, UUID receiverId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        if (!notification.getReceiverId().equals(receiverId)) {
            throw new NotificationForBiddenException(notificationId);
        }

        notificationRepository.delete(notification);
    }
}
