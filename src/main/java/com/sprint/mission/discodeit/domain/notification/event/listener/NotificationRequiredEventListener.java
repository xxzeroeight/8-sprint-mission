package com.sprint.mission.discodeit.domain.notification.event.listener;

import com.sprint.mission.discodeit.domain.message.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.domain.notification.service.NotificationService;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.event.RoleUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "discodeit.notification", name = "listener", havingValue = "event", matchIfMissing = true)
@Component
public class NotificationRequiredEventListener
{
    private final ReadStatusRepository readStatusRepository;
    private final NotificationService notificationService;

    @Async("notificationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(MessageCreatedEvent event) {
        List<ReadStatus> readStatuses = readStatusRepository
                .findAllByChannelIdAndNotificationEnabledTrue(event.channelId());

        String title = event.authorName() + " [#" + event.channelName() + "]";
        String content = event.content();

        for (ReadStatus readStatus : readStatuses) {
            UUID receiverId = readStatus.getUser().getId();

            // 본인은 알림 제외
            if (receiverId.equals(event.authorId())) {
                continue;
            }

            try {
                notificationService.create(receiverId, title, content);
            } catch (Exception e) {
                log.error("알림 생성 실패: receiverId={}", receiverId, e);
            }
        }
    }

    @Async("notificationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(RoleUpdatedEvent event) {
        String title = "권한이 변경되었습니다.";
        String content = event.oldRole() + " -> " + event.newRole();

        try {
            notificationService.create(event.userId(), title, content);
        } catch (Exception e) {
            log.error("권한 변경 알림 생성 실패: userId={}", event.userId(), e);
        }
    }
}
