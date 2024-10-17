package com.example.itemservice.messagequeue;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.exception.InsufficientStockException;
import com.example.itemservice.exception.ItemNotFoundException;
import com.example.itemservice.messagequeue.message.OrderFailMessage;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.request.ItemStock;
import com.example.itemservice.request.ItemStockUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ItemRepository itemRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper mapper;



    @KafkaListener(topics = "item-add-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void updateStock(String message) {


        consumeMessageValue(message, true);

    }



    @KafkaListener(topics = "item-reduce-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void reduceStock(String message) {


        consumeMessageValue(message, false);

    }

//    private void consumeMessageValue(String message, boolean isAddition) {
//
//        log.info("message = {}", message);
//
//        try {
//
//            ItemStockUpdate itemUpdate = mapper.readValue(message, ItemStockUpdate.class);
//
//
//            for (ItemStock itemStock : itemUpdate.getItems()) {
//
//                String uuid = itemStock.getItemUUID();
//                Integer qty = itemStock.getQuantity();
//
//
//
//                Item item = itemRepository.findByItemUUID(uuid)
//                        .orElseThrow(ItemNotFoundException::new);
//
//                item.updateQuantity(qty, isAddition);
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }

    private void consumeMessageValue(String message, boolean isAddition) {
        log.info("message = {}", message);

        try {
            ItemStockUpdate itemUpdate = mapper.readValue(message, ItemStockUpdate.class);

            for (ItemStock itemStock : itemUpdate.getItems()) {
                String uuid = itemStock.getItemUUID();
                Integer qty = itemStock.getQuantity();

                boolean isSuccess = false;
                int retryCount = 0;
                int maxRetries = 5;
                long retryDelay = 500L;

                while (!isSuccess && retryCount < maxRetries) {
                    Boolean lock = redisTemplate.opsForValue().setIfAbsent(uuid, "LOCK", Duration.ofMillis(3_000));

                    if (Boolean.TRUE.equals(lock)) {
                        try {
                            Item item = itemRepository.findByItemUUID(uuid)
                                    .orElseThrow(ItemNotFoundException::new);

                            item.updateQuantity(qty, isAddition);

                            isSuccess = true;
                        } catch (InsufficientStockException e){
                            log.warn("재고 부족으로 주문 실패: {}", itemUpdate.getOrderUUID());

                            OrderFailMessage orderFailMessage = new OrderFailMessage(itemUpdate.getOrderUUID());
                            orderFailMessage.setMessage("Insufficient Stock: 재고 부족");
                            kafkaProducer.send("order-fail-topic", orderFailMessage);

                            throw e;


                        }
                        finally {
                            redisTemplate.delete(uuid);
                        }
                    } else {
                        retryCount++;
                        Thread.sleep(retryDelay);
                    }
                }

                if (!isSuccess) {
                    OrderFailMessage orderFailMessage = new OrderFailMessage(itemUpdate.getOrderUUID());
                    orderFailMessage.setMessage("Lock Time Out");

                    kafkaProducer.send("order-fail-topic", orderFailMessage);

                }
            }
        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
