package com.example.itemservice.service;

import com.example.itemservice.auth.UserInfo;
import com.example.itemservice.client.ImageServiceClient;
import com.example.itemservice.client.ReviewServiceClient;
import com.example.itemservice.client.UserServiceClient;
import com.example.itemservice.data.ImageType;
import com.example.itemservice.data.UserType;
import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.log.OrderEventLog;
import com.example.itemservice.domain.outbox.ItemOutbox;
import com.example.itemservice.exception.InsufficientStockException;
import com.example.itemservice.exception.ItemNotFoundException;
import com.example.itemservice.exception.UnauthorizedException;
import com.example.itemservice.messagequeue.message.OrderFailMessage;
import com.example.itemservice.repository.EventRepository;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.repository.OutboxRepository;
import com.example.itemservice.request.*;
import com.example.itemservice.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageServiceClient imageServiceClient;
    private final ReviewServiceClient reviewServiceClient;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper mapper;
    private final OutboxRepository outboxRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public PageResponse items(String search, Category category, int page) {


        Page<Item> items = itemRepository.getPage(search, category, page);

        List<String> itemUUIDs = items.getContent().stream().map(Item::getItemUUID).toList();

        log.info("uuids = {}", itemUUIDs);

        Map<String,Double> responses = reviewServiceClient.getAverageScores(itemUUIDs);

        log.info("response = {}", responses);

        for (String key : responses.keySet()) {
            log.info("key ={}, value ={}", key, responses.get(key));
        }

        return PageResponse.builder()
                .isFirst(items.isFirst())
                .isLast(items.isFirst())
                .items(items.map(item -> {
                    ItemResponse itemResponse = new ItemResponse(item);
                    itemResponse.setScore(responses.get(item.getItemUUID()));
                    return itemResponse;
                }).toList())
                .totalElement(items.getTotalElements())
                .totalPage(items.getTotalPages())
                .build();



    }
    @Transactional(readOnly = true)
    public ItemDetailResponse getItem(String itemUUID) {

        Item item = itemRepository.findByItemUUID(itemUUID).orElseThrow(ItemNotFoundException::new);

        log.info("item uuid ={}",item.getItemUUID());
        ItemDetailResponse response = new ItemDetailResponse(item);
        response.linkUrls(imageServiceClient.getURls(item.getItemUUID(), ImageType.ITEM));
        response.linkScore(reviewServiceClient.getAverageScore(item.getItemUUID()));

        log.info("response = {}", response);

        return response;


    }



    public Long addItem(ItemRequest itemRequest , UserInfo userInfo) {

        userTypeCheck(userInfo.getUuid());

        Item item = itemRepository.save(
                Item.builder()
                        .name(itemRequest.getName())
                        .itemDescription(itemRequest.getItemDescription())
                        .price(itemRequest.getPrice())
                        .stock(itemRequest.getStock())
                        .category(itemRequest.getCategory())
                        .itemUUID(itemRequest.getItemUUID())
                        .userUUID(userInfo.getUuid())
                        .build()
        );

        return item.getId();

    }


    public void deleteItem(Long itemId, UserInfo userInfo) {

        Item item = itemAuthCheck(itemId, userInfo);

        imageServiceClient.deleteItemImage(item.getItemUUID());
        itemRepository.delete(item);

    }



    public BigDecimal amount(List<ItemQuantity> quantities) {
        int sum = 0;
        for (ItemQuantity quantity : quantities) {
            Item item = itemRepository.findByItemUUID(quantity.getItemUUID()).orElseThrow(ItemNotFoundException::new);
            sum += item.getPrice() * quantity.getQuantity();
        }

        return new BigDecimal(sum);

    }

    public List<CartItemResponse> getCartItems(Map<String,Integer> infos) {

        return itemRepository.findItemInUUIDs(infos.keySet()).stream().map(item -> {
            CartItemResponse cartItem = new CartItemResponse(item);
            cartItem.setQuantity(infos.get(item.getItemUUID()));
            cartItem.calItemPrice();
            return cartItem;
        }).toList();
    }

    public void update(Long itemId, ItemUpdate itemUpdate, UserInfo userInfo) {

        Item item = itemAuthCheck(itemId, userInfo);

        item.update(itemUpdate);

    }


    private Item itemAuthCheck(Long itemId, UserInfo userInfo) {

        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        if (!item.getUserUUID().equals(userInfo.getUuid())) {

            userTypeCheck(userInfo.getUuid());

        }


        return item;
    }

    private void userTypeCheck(String userUUID) {
        if (!userServiceClient.getType(userUUID).equals(UserType.ADMIN)) {
            throw new UnauthorizedException();
        }
    }


//    public void reduceQuantity(List<ItemQuantity> quantities) {
//
//        for (ItemQuantity quantity : quantities) {
//            Item item = itemRepository.findByItemUUID(quantity.getItemUUID()).orElseThrow(() -> new RuntimeException("아이템 없음"));
//            item.reduceQuantity(quantity.getQuantity());
//        }
//
//    }

//    public void addQuantity(List<ItemQuantity> quantities) {
//
//        for (ItemQuantity quantity : quantities) {
//            Item item = itemRepository.findByItemUUID(quantity.getItemUUID()).orElseThrow(() -> new RuntimeException("아이템 없음"));
//            item.addQuantity(quantity.getQuantity());
//        }
//    }

    public void processStock(String message, boolean isAddition) {
        log.info("message = {}", message);


        try {
            ItemStockUpdate itemUpdate = mapper.readValue(message, ItemStockUpdate.class);
            boolean allSuccess = true;

            if (eventRepository.existsByOrderUUID(itemUpdate.getOrderUUID())) {
                log.info("OrderUUID 중복. orderUUID={}", itemUpdate.getOrderUUID());
                return;
            }



            for (ItemStock itemStock : itemUpdate.getItems()) {
                String uuid = itemStock.getItemUUID();
                Integer qty = itemStock.getQuantity();

                boolean isSuccess = false;
                int retryCount = 0;
                int maxRetries = 5;
                long retryDelay = 500L;

                while (!isSuccess && retryCount < maxRetries) {
                    Boolean lock = redisTemplate.opsForValue().setIfAbsent(uuid, "ITEM:LOCK:", Duration.ofMillis(3_000));

                    if (Boolean.TRUE.equals(lock)) {
                        try {
                            Item item = itemRepository.findByItemUUID(uuid)
                                    .orElseThrow(ItemNotFoundException::new);

                            item.updateQuantity(qty, isAddition);

                            isSuccess = true;
                        } catch (InsufficientStockException e){
                            log.warn("재고 부족으로 주문 실패: {}", itemUpdate.getOrderUUID());

                            createOutbox(itemUpdate, "Insufficient Stock: 재고 부족");

//                            kafkaProducer.send("order-fail-topic", orderFailMessage);

                            allSuccess = false;


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
                    createOutbox(itemUpdate, "Lock Time Out");

//                    kafkaProducer.send("order-fail-topic", orderFailMessage);
                    allSuccess = false;

                }
            }

            if (allSuccess) {
                eventRepository.save(new OrderEventLog(itemUpdate.getOrderUUID()));
            }

        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void createOutbox(ItemStockUpdate itemUpdate, String message) throws JsonProcessingException {
        OrderFailMessage orderFailMessage = new OrderFailMessage(itemUpdate.getOrderUUID());
        orderFailMessage.setMessage(message);

        String failMessage = mapper.writeValueAsString(orderFailMessage);

        outboxRepository.save(
                ItemOutbox.builder()
                        .eventType("ORDER-FAIL-LOCK")
                        .orderUUID(itemUpdate.getOrderUUID())
                        .message(failMessage)
                        .build()
        );
    }


}
