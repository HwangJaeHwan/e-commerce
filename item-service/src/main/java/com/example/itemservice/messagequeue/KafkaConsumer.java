package com.example.itemservice.messagequeue;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.exception.ItemNotFoundException;
import com.example.itemservice.repository.ItemRepository;
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

    private final ItemRepository itemRepository;
    private final ObjectMapper mapper;



    @KafkaListener(topics = "item-add-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void updateStock(String message) {


        consumeMessageValue(message, true);

    }



    @KafkaListener(topics = "item-reduce-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void reduceStock(String message) {


        consumeMessageValue(message, false);

    }

    private void consumeMessageValue(String message, boolean isAddition) {

        HashMap<Object, Object> map;

        try {
            map = mapper.readValue(message, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String uuid = (String) map.get("itemUUID");
        Integer qty = (Integer) map.get("quantity");
        Item item = itemRepository.findByItemUUID(uuid).orElseThrow(ItemNotFoundException::new);

        item.updateQuantity(qty, isAddition);


    }
}
