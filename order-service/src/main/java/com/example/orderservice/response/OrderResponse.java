package com.example.orderservice.response;

import com.example.orderservice.domain.OrderItem;
import com.example.orderservice.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class OrderResponse {

    private String orderUUID;
    private LocalDateTime orderDate;
    private String city;
    private String street;
    private String zipcode;
    private OrderStatus orderStatus;

    private List<ItemResponse> items = new ArrayList<>();

    private int totalPrice;
    @Builder
    public OrderResponse(String orderUUID, LocalDateTime orderDate, String city, String street,
                         String zipcode, OrderStatus orderStatus) {
        this.orderUUID = orderUUID;
        this.orderDate = orderDate;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.orderStatus = orderStatus;
    }

    public void addItems(List<ItemResponse> items) {
        this.items.addAll(items);
    }

    public void calTotalPrice(List<OrderItem> items) {
        this.totalPrice = items.stream().mapToInt(o -> o.getQuantity() * o.getPrice()).sum();
    }



}
