package com.sprint.mission.discodeit.domain.notification.event.listener;

public final class KafkaTopics
{
    public static final String MESSAGE_CREATED = "discodeit.MessageCreatedEvent";
    public static final String ROLE_UPDATED = "discodeit.RoleUpdatedEvent";
    public static final String S3_UPLOAD_FAILED = "discodeit.S3UploadFailedEvent";

    private KafkaTopics() {}
}
