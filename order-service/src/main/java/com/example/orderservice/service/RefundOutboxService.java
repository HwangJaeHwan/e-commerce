package com.example.orderservice.service;

import com.example.orderservice.domain.outbox.OrderOutbox.Status;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.repository.OrderOutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RefundOutboxService {
    private final OrderOutboxRepository repository;
    private final KafkaProducer producer;
    private final TransactionTemplate transaction;
    private final long timeoutMillis;

    public RefundOutboxService(OrderOutboxRepository repository, KafkaProducer producer, PlatformTransactionManager manager,
                         @Value("${outbox.send-timeout-ms:10000}") long timeoutMillis) {
        this.repository = repository;
        this.producer = producer;
        this.timeoutMillis = timeoutMillis;
        this.transaction = new TransactionTemplate(manager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public void publishBatch(Status status) {
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

    public void processOutbox(Long id, Status expectedStatus) {
        transaction.executeWithoutResult(tx -> repository.findForPublishing(id).ifPresent(outbox -> {
            if (outbox.getStatus() != expectedStatus || outbox.getStatus() == Status.SENT) return;
            try {
                producer.sendRaw("payment-cancel-topic", outbox.getOrderUUID(), outbox.getMessage())
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
