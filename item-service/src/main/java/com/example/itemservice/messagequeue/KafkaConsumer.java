package com.example.itemservice.messagequeue;


import com.example.itemservice.service.StockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {


    private final StockService stockService;



    @KafkaListener(topics = "item-add-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void updateStock(String message) {


        stockService.processStock(message, true);

    }



    @KafkaListener(topics = "item-reduce-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void reduceStock(String message) {


        stockService.processStock(message, false);

    }



}
