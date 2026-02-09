package com.example.itemservice.messagequeue;

import com.example.itemservice.messagequeue.message.OrderFailMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {


        kafkaTemplate.send(topic, message)
                .thenAccept(result -> log.info("Kafka 전송 성공 → topic={}",
                            result.getRecordMetadata().topic()))
                    .exceptionally(ex -> {
                        log.error("Kafka 전송 실패", ex);
                        return null;
                    });






    }




}
