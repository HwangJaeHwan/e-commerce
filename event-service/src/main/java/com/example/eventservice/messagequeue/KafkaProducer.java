package com.example.eventservice.messagequeue;

import com.example.eventservice.request.CouponRequest;
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

    public void send(String topic, CouponRequest couponRequest) {

        String message = null;

        try {
            message = mapper.writeValueAsString(couponRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        kafkaTemplate.send(topic, message);


    }




}