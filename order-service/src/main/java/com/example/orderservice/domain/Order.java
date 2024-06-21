package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Table(name = "Orders")
@NoArgsConstructor(access = PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private String userUUID;
    private String orderUUID;
    private LocalDateTime orderDate;

    private String city;
    private String street;
    private String zipcode;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Builder
    public Order(List<OrderItem> orderItems, String userUUID, String orderUUID, LocalDateTime orderDate, String city, String street, String zipcode, OrderStatus orderStatus) {
        this.orderItems = orderItems;
        this.userUUID = userUUID;
        this.orderUUID = orderUUID;
        this.orderDate = orderDate;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.orderStatus = orderStatus;
    }


    public void addItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.linkOrder(this);
    }

    public void changeStatus(OrderStatus status) {
        this.orderStatus = status;
    }
}
