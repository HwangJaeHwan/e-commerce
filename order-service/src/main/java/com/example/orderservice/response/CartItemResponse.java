package com.example.orderservice.response;

import com.example.orderservice.domain.CartItem;
import lombok.Getter;

@Getter
public class CartItemResponse {

    private String itemUUID;

    private Integer quantity;

    public CartItemResponse(CartItem cartItem) {
        this.itemUUID = cartItem.getItemUUID();
        this.quantity = cartItem.getQuantity();
    }
}
