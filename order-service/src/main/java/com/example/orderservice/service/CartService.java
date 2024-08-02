package com.example.orderservice.service;

import com.example.orderservice.client.ItemServiceClient;
import com.example.orderservice.domain.CartItem;
import com.example.orderservice.domain.ShoppingCart;
import com.example.orderservice.exception.CartNotFoundException;
import com.example.orderservice.repository.CartItemRepository;
import com.example.orderservice.repository.ShoppingCartRepository;
import com.example.orderservice.response.CartItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository itemRepository;
    private final ItemServiceClient itemServiceClient;

    public List<CartItemResponse> getCartItems(String userUUID) {
        ShoppingCart cart = cartRepository.findByUserUUID(userUUID).orElseThrow(CartNotFoundException::new);

        List<CartItemResponse> responses = itemServiceClient.cartItemsInfo(itemRepository.findAllByCart(cart).stream()
                .collect(Collectors.toMap(CartItem::getItemUUID, CartItem::getQuantity)));

        for (CartItemResponse response : responses) {
            log.info("data = {}",response);
        }

        return responses;

    }
}
