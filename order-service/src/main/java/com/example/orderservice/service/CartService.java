package com.example.orderservice.service;

import com.example.orderservice.domain.ShoppingCart;
import com.example.orderservice.exception.CartNotFoundException;
import com.example.orderservice.repository.CartItemRepository;
import com.example.orderservice.repository.ShoppingCartRepository;
import com.example.orderservice.response.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository itemRepository;

    public List<CartItemResponse> getCartItems(String userUUID) {
        ShoppingCart cart = cartRepository.findByUserUUID(userUUID).orElseThrow(CartNotFoundException::new);
        return itemRepository.findAllByCart(cart).stream().map(CartItemResponse::new).toList();

    }
}
