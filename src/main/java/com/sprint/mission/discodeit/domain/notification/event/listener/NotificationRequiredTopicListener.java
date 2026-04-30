package com.sprint.mission.discodeit.domain.notification.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.binarycontent.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.domain.message.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.domain.notification.service.NotificationService;
import com.sprint.mission.discodeit.domain.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.domain.Role;
import com.sprint.mission.discodeit.domain.user.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@ConditionalOnProperty(prefix = "discodeit.notification", name = "listener", havingValue = "kafka")
@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationRequiredTopicListener
{
    private final NotificationService notificationService;
    private final ReadStatusRepository readStatusRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @KafkaListener(topics = KafkaTopics.MESSAGE_CREATED)
    public void onMessageCreatedEvent(String kafkaEvent) {
        try {
            MessageCreatedEvent event = objectMapper.readValue(kafkaEvent, MessageCreatedEvent.class);

            List<ReadStatus> readStatuses = readStatusRepository
                    .findAllByChannelIdAndNotificationEnabledTrue(event.channelId());

            String title = event.authorName() + " [#" + event.channelName() + "]";
            String content = event.content();

            readStatuses.stream()
                    .filter(rs -> !rs.getUser().getId().equals(event.authorId()))
                    .forEach(rs -> notificationService.create(
                            rs.getUser().getId(),
                            title,
                            content
                    ));
        } catch (JsonProcessingException e) {
            log.error("MessageCreatedEvent 역직렬화 실패: {}", kafkaEvent, e);
        }
    }

    @KafkaListener(topics = KafkaTopics.ROLE_UPDATED)
    public void onRoleUpdatedEvent(String kafkaEvent) {
        try {
            RoleUpdatedEvent event = objectMapper.readValue(kafkaEvent, RoleUpdatedEvent.class);

            String title = "권한이 변경되었습니다.";
            String content = event.oldRole() + " -> " + event.newRole();

            notificationService.create(event.userId(), title, content);
        } catch (JsonProcessingException e) {
            log.error("RoleUpdatedEvent 역직렬화 실패: {}", kafkaEvent, e);
        }
    }

    @KafkaListener(topics = KafkaTopics.S3_UPLOAD_FAILED)
    public void onS3UploadFailedEvent(String kafkaEvent) {
        try {
            S3UploadFailedEvent event = objectMapper.readValue(kafkaEvent, S3UploadFailedEvent.class);

            String title = "BinaryContent 저장 실패";
            String content = "BinaryContentId " + event.binaryContentId();

            userRepository.findAllByRole(Role.ADMIN)
                    .forEach(admin ->
                        notificationService.create(admin.getId(), title, content)
                    );
        } catch (JsonProcessingException e) {
            log.error("S3UploadFailedEvent 역직렬화 실패: {}", kafkaEvent, e);
        }
    }
}
