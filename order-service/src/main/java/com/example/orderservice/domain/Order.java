package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @OneToMany(mappedBy = "orderItem_id", cascade = REMOVE)
    private List<OrderItem> orderItems = new ArrayList<>();

    private String userUUID;
    private String orderUUID;
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;




}
