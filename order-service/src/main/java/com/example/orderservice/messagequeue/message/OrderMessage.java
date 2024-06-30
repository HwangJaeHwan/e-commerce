package com.example.orderservice.messagequeue.message;

import com.example.orderservice.request.ItemQuantity;
import com.example.orderservice.request.ItemRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderMessage {

    private final List<ItemQuantity> items;


    public OrderMessage(List<ItemQuantity> items) {
        this.items = items;
    }
}
