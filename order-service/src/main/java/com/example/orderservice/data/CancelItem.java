package com.example.orderservice.data;

import com.example.orderservice.domain.OrderItem;
import lombok.Getter;

@Getter
public class CancelItem {

    private String itemUUID;
    private int quantity;

    public CancelItem(OrderItem orderItem) {
        this.itemUUID = orderItem.getItemUUID();
        this.quantity = orderItem.getQuantity();
    }
}
