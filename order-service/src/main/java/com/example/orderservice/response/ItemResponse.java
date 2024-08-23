package com.example.orderservice.response;

import com.example.orderservice.domain.OrderItem;
import lombok.Getter;

@Getter
public class ItemResponse {
    private Long itemId;
    private String name;
    private String itemUUID;
    private int quantity;
    private int price;


    public ItemResponse(OrderItem orderItem) {
        this.itemId = orderItem.getItemId();
        this.name = orderItem.getName();
        this.itemUUID = orderItem.getItemUUID();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}
