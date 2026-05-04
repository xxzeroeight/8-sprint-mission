package com.sprint.mission.discodeit.domain.binarycontent.event.listener;

import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContentStatus;
import com.sprint.mission.discodeit.domain.binarycontent.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class BinaryContentEventListener
{
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    @Async("binaryContentTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBinaryContentCreatedEvent(BinaryContentCreatedEvent event) {
        try {
            binaryContentStorage.save(event.binaryContentId(), event.bytes());
            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.SUCCESS);
        } catch (Exception ex) {
            log.error("바이너리 컨텐츠 저장 실패 - binaryContentId: {}", event.binaryContentId(), ex);
            binaryContentService.updateStatus(event.binaryContentId(), BinaryContentStatus.FAIL);
        }
    }
}
