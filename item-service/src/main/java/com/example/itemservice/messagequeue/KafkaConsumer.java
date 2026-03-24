package com.example.itemservice.messagequeue;


import com.example.itemservice.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KafkaConsumer {


    private final ItemService itemService;



    @KafkaListener(topics = "item-add-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void updateStock(String message) {


        itemService.processStock(message, true);

    }



    @KafkaListener(topics = "item-reduce-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void reduceStock(String message) {


        itemService.processStock(message, false);

    }



}
