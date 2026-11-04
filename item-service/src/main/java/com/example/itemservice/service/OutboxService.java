package com.example.itemservice.service;

import com.example.itemservice.domain.outbox.OutboxStatus;
import com.example.itemservice.messagequeue.KafkaProducer;
import com.example.itemservice.repository.OutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OutboxService {
    private final OutboxRepository repository;
    private final KafkaProducer producer;
    private final TransactionTemplate transaction;
    private final long timeoutMillis;

    public OutboxService(OutboxRepository repository, KafkaProducer producer, PlatformTransactionManager manager,
                         @Value("${outbox.send-timeout-ms:10000}") long timeoutMillis) {
        this.repository = repository;
        this.producer = producer;
        this.timeoutMillis = timeoutMillis;
        this.transaction = new TransactionTemplate(manager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public void publishBatch(OutboxStatus status) {
        var ids = repository.findTop100ByStatusOrderByIdAsc(status).stream().map(o -> o.getId()).toList();
        for (Long id : ids) {
            try {
                processOutbox(id, status);
            } catch (RuntimeException e) {
                log.error("Outbox transaction failed. id={}", id, e);
            }
            if (Thread.currentThread().isInterrupted()) break;
        }
    }

    public void processOutbox(Long id, OutboxStatus expectedStatus) {
        transaction.executeWithoutResult(tx -> repository.findForPublishing(id).ifPresent(outbox -> {
            if (outbox.getStatus() != expectedStatus || outbox.getStatus() == OutboxStatus.SENT) return;
            try {
                producer.send("order-fail-topic", outbox.getOrderUUID(), outbox.getMessage())
                        .get(timeoutMillis, TimeUnit.MILLISECONDS);
                outbox.markPublished();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                outbox.markFailed();
                log.warn("Outbox publication interrupted. id={}", id);
            } catch (Exception e) {
                outbox.markFailed();
                log.error("Outbox publication failed. id={}", id, e);
            }
        }));
    }
}
