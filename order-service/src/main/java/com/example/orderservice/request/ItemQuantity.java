package com.example.orderservice.request;

import com.example.orderservice.domain.OrderItem;
import lombok.Getter;

@Getter
public class ItemQuantity {

    private String itemUUID;
    private int quantity;


    public ItemQuantity(ItemRequest itemRequest) {
        this.itemUUID = itemRequest.getItemUUID();
        this.quantity = itemRequest.getQuantity();
    }

    public ItemQuantity(OrderItem orderItem) {
        this.itemUUID = orderItem.getItemUUID();
        this.quantity = orderItem.getQuantity();
    }
}
