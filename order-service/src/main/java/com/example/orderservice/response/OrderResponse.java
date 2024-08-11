package com.example.orderservice.response;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderItem;
import com.example.orderservice.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class OrderResponse {

    private Long orderId;
    private String orderUUID;
    private LocalDateTime orderDate;
    private String address;
    private String detailAddress;
    private String zipcode;
    private String phoneNumber;
    private OrderStatus orderStatus;

    private List<ItemResponse> items = new ArrayList<>();

    private int totalPrice;
    @Builder
    public OrderResponse(Long orderId, String orderUUID, LocalDateTime orderDate, String address,
                         String detailAddress, String zipcode, String phoneNumber, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderUUID = orderUUID;
        this.orderDate = orderDate;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.orderStatus = orderStatus;
    }

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.orderUUID = order.getOrderUUID();
        this.orderDate = order.getOrderDate();
        this.address = order.getAddress();
        this.detailAddress = order.getDetailAddress();
        this.zipcode = order.getZipcode();
        this.phoneNumber = order.getPhoneNumber();
        this.orderStatus = order.getOrderStatus();
    }

    public void addItems(List<ItemResponse> items) {
        this.items.addAll(items);
    }

    public void calTotalPrice(List<OrderItem> items) {
        this.totalPrice = items.stream().mapToInt(o -> o.getQuantity() * o.getPrice()).sum();
    }



}
