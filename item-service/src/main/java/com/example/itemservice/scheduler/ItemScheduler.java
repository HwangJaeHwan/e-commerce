package com.example.itemservice.scheduler;

import com.example.itemservice.domain.outbox.OutboxStatus;
import com.example.itemservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemScheduler {
    private final OutboxService outboxService;

    @Scheduled(fixedDelayString = "${outbox.poll-delay-ms:500}")
    public void publishOutboxEvents() {
        outboxService.publishBatch(OutboxStatus.PENDING);
    }

    @Scheduled(fixedDelayString = "${outbox.retry-delay-ms:10000}")
    public void failOutboxEvents() {
        outboxService.publishBatch(OutboxStatus.FAILED);
    }
}
