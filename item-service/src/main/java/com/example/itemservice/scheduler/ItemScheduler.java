package com.example.itemservice.scheduler;

import com.example.itemservice.domain.outbox.ItemOutbox;
import com.example.itemservice.domain.outbox.OutboxStatus;
import com.example.itemservice.messagequeue.KafkaProducer;
import com.example.itemservice.repository.OutboxRepository;
import com.example.itemservice.service.OutboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ItemScheduler {

    private final OutboxRepository outboxRepository;
    private final OutboxService outboxService;

    @Scheduled(fixedRate = 500)
    public void publishOutboxEvents() {

        process(OutboxStatus.PENDING);


    }

    @Scheduled(fixedRate = 10000)
    public void failOutboxEvents() {

        process(OutboxStatus.FAILED);


    }


    private void process(OutboxStatus status) {
        List<ItemOutbox> outboxes = outboxRepository.fetchBatchForProcessing(status);

        for (ItemOutbox outbox : outboxes) {

            outboxService.processOutbox(outbox);


        }
    }



}
