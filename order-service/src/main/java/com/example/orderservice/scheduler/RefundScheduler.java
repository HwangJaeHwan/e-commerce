package com.example.orderservice.scheduler;

import com.example.orderservice.domain.outbox.OrderOutbox.Status;
import com.example.orderservice.service.RefundOutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundScheduler {
    private final RefundOutboxService outboxService;

    @Scheduled(fixedDelayString = "${outbox.poll-delay-ms:500}")
    public void publishOutboxEvents() {
        outboxService.publishBatch(Status.PENDING);
    }

    @Scheduled(fixedDelayString = "${outbox.retry-delay-ms:10000}")
    public void failOutboxEvents() {
        outboxService.publishBatch(Status.FAILED);
    }
}
