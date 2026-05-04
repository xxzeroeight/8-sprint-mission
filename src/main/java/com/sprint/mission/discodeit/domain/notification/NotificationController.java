package com.sprint.mission.discodeit.domain.notification;

import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.domain.notification.dto.entity.NotificationDto;
import com.sprint.mission.discodeit.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@RestController
public class NotificationController
{
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(@AuthenticationPrincipal DiscodeitUserDetails userDetails)
    {
        List<NotificationDto> notificationDtos = notificationService.findAllByReceiverId(userDetails.getUserDto().id());

        return ResponseEntity.ok(notificationDtos);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID notificationId,
                                                   @AuthenticationPrincipal DiscodeitUserDetails userDetails)
    {
        notificationService.delete(notificationId, userDetails.getUserDto().id());

        return ResponseEntity.noContent().build();
    }
}
