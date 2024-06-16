package com.example.orderservice.response;

import com.example.orderservice.domain.OrderItem;
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

    public void addItems(List<ItemResponse> items) {
        this.items.addAll(items);
    }

    public void calTotalPrice(List<OrderItem> items) {
        this.totalPrice = items.stream().mapToInt(o -> o.getQuantity() * o.getPrice()).sum();
    }



}
