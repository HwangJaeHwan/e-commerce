package com.example.orderservice.messagequeue;

import com.example.orderservice.domain.CartItem;
import com.example.orderservice.domain.ShoppingCart;
import com.example.orderservice.repository.CartItemRepository;
import com.example.orderservice.repository.ShoppingCartRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository cartRepository;
    private final ObjectMapper mapper;


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


}
