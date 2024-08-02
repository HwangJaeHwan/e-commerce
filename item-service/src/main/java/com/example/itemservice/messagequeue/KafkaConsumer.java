package com.example.itemservice.messagequeue;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.exception.ItemNotFoundException;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.request.ItemStock;
import com.example.itemservice.request.ItemStockUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Slf4j
@Transactional
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

        log.info("message = {}", message);

        try {

            ItemStockUpdate itemUpdate = mapper.readValue(message, ItemStockUpdate.class);


            for (ItemStock itemStock : itemUpdate.getItems()) {

                String uuid = itemStock.getItemUUID();
                Integer qty = itemStock.getQuantity();



                Item item = itemRepository.findByItemUUID(uuid)
                        .orElseThrow(ItemNotFoundException::new);

                item.updateQuantity(qty, isAddition);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
