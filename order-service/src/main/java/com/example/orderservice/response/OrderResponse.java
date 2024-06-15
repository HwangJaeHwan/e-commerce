package com.example.orderservice.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderResponse {

    private String userUUID;
    private String orderUUID;
    private LocalDateTime orderDate;

    private List<ItemResponse> items = new ArrayList<>();

    private int totalPrice;
    @Builder
    public OrderResponse(String userUUID, String orderUUID, LocalDateTime orderDate, int totalPrice) {
        this.userUUID = userUUID;
        this.orderUUID = orderUUID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    void addItem(ItemResponse itemResponse) {
        this.items.add(itemResponse);

    }
}
