package com.example.orderservice.messagequeue.message;

import com.example.orderservice.request.ItemQuantity;
import com.example.orderservice.request.ItemRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderMessage {

    private final String orderUUID;
    private final List<ItemQuantity> items;


    public OrderMessage(String orderUUID, List<ItemQuantity> items) {
        this.orderUUID = orderUUID;
        this.items = items;
    }
}
