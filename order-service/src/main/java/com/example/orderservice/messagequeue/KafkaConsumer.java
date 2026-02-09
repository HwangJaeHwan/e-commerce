package com.example.orderservice.messagequeue;

import com.example.orderservice.domain.*;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.messagequeue.message.OrderMessage;
import com.example.orderservice.repository.CartItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ShoppingCartRepository;
import com.example.orderservice.request.ItemQuantity;
import com.example.orderservice.request.ItemRequest;
import com.example.orderservice.request.OrderRequest;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository cartRepository;
    private final KafkaProducer kafkaProducer;
    private final OrderRepository orderRepository;
    private final ObjectMapper mapper;


    @Transactional
    @KafkaListener(topics = "cart-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void consume(String message) {

        HashMap<String, Object> map;
        ShoppingCart cart;
        try {
            map = mapper.readValue(message, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        String itemUUID = (String) map.get("itemUUID");
        String userUUID = (String) map.get("userUUID");
        Integer quantity = (Integer) map.get("quantity");


        Optional<ShoppingCart> cartOptional = cartRepository.findByUserUUID(userUUID);
        cart = cartOptional.orElseGet(() -> cartRepository.save(new ShoppingCart(userUUID)));


        Optional<CartItem> itemOptional = cartItemRepository.findByMessage(cart, itemUUID);

        if (itemOptional.isEmpty()) {
            cartItemRepository.save(new CartItem(cart, itemUUID, quantity));
            return;
        }

        CartItem cartItem = itemOptional.get();

        if (quantity == 0) {
            cartItemRepository.delete(cartItem);
            return;
        }

        cartItem.updateCartItemQuantity(quantity);


    }


    @Transactional
    @KafkaListener(topics = "order-create-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void consumeOrderCreate(String message) {


        try {

            OrderRequest orderRequest = mapper.readValue(message, OrderRequest.class);


            if (orderRepository.findByOrderUUID(orderRequest.getOrderUUID()).isPresent()) {
                return;
            }

            Order order = Order.builder()
                    .orderStatus(OrderStatus.ORDER)
                    .userUUID(orderRequest.getUserUUID())
                    .orderUUID(orderRequest.getOrderUUID())
                    .orderDate(LocalDateTime.now())
                    .name(orderRequest.getName())
                    .impUid(orderRequest.getImpUid())
                    .address(orderRequest.getAddress())
                    .detailAddress(orderRequest.getDetailAddress())
                    .zipcode(orderRequest.getZipcode())
                    .phoneNumber(orderRequest.getPhoneNumber())
                    .build();

            for (ItemRequest item : orderRequest.getItems()) {
                order.addItem(
                        OrderItem.builder()
                                .name(item.getName())
                                .itemUUID(item.getItemUUID())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build()
                );
            }

            if (orderRequest.getFromCart()) {
                cartRepository.findByUserUUID(orderRequest.getUserUUID()).ifPresent(cartItemRepository::deleteAllByCart);
            }


            orderRepository.save(order);

            kafkaProducer.send("item-reduce-topic"
                    , new OrderMessage(order.getOrderUUID(), orderRequest.getItems().stream().map(ItemQuantity::new).toList()));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }






    @Transactional
    @KafkaListener(topics = "order-fail-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void consumeOrderFail(String message) {

        HashMap<String, Object> map;

        try {
            map = mapper.readValue(message, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String errorMessage = (String) map.get("message");
        String orderUUID =(String) map.get("orderUUID");
        log.error("Order Fail Message= {}", errorMessage);

        Order order = orderRepository.findByOrderUUID(orderUUID).orElseThrow(OrderNotFoundException::new);

        order.changeStatus(OrderStatus.CANCELLED);
        order.setCancelReason(errorMessage);




    }


}
