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
public class OrderTransfer {

    private Long orderId;
    private String orderUUID;
    private String userUUID;
    private OrderStatus orderStatus;

    private List<ItemResponse> items = new ArrayList<>();


    public OrderTransfer(Order order) {
        this.orderId = order.getId();
        this.orderUUID = order.getOrderUUID();
        this.userUUID = order.getUserUUID();
        this.orderStatus = order.getOrderStatus();

    }

    public void addItems(List<ItemResponse> items) {
        this.items.addAll(items);
    }
}
