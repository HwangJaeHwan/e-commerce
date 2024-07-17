package com.example.orderservice.messagequeue;

import com.example.orderservice.domain.ShoppingCart;
import com.example.orderservice.repository.CartRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CartRepository cartRepository;
    private final ObjectMapper mapper;


    @KafkaListener(topics = "cart-add-topic")
    public void addCart(String message) {

        HashMap<Object, Object> map;

        try {
            map = mapper.readValue(message, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        String itemUUID = (String) map.get("itemUUID");
        String userUUID = (String) map.get("userUUID");
        Integer quantity = (Integer) map.get("quantity");


        cartRepository.save(new ShoppingCart(userUUID, itemUUID, quantity));


    }

}
