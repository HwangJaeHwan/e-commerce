package com.example.orderservice.messagequeue;

import com.example.orderservice.messagequeue.message.OrderMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public java.util.concurrent.CompletableFuture<org.springframework.kafka.support.SendResult<String, String>>
            sendRaw(String topic, String key, String message) {
        return kafkaTemplate.send(topic, key, message);
    }

    public void send(String topic, OrderMessage orderMessage) {

        String message;

        try {
            message = mapper.writeValueAsString(orderMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(topic, message);


    }




}
