package com.example.itemservice.service;

import com.example.itemservice.domain.outbox.ItemOutbox;
import com.example.itemservice.messagequeue.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxService {

    private final KafkaProducer kafkaProducer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOutbox(ItemOutbox outbox) {

        try {
            kafkaProducer.send("order-fail-topic", outbox.getMessage());
            outbox.markPublished();

            log.info("Outbox publish success. id={}", outbox.getId());

        } catch (Exception e) {
            log.error("Outbox publish failed. id={}, error={}", outbox.getId(), e.getMessage());
            outbox.markFailed();
        }
    }



}
