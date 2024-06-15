package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "orderItem_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Order order;

    private String itemUUID;

    private int quantity;

    private int price;

    @Builder
    public OrderItem(String itemUUID, int quantity, int price) {
        this.itemUUID = itemUUID;
        this.quantity = quantity;
        this.price = price;
    }

    public void linkOrder(Order order) {
        this.order = order;
    }


}
