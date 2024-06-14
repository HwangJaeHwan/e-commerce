package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "orderItem_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Order order;

    private String name;

    private String itemUUID;

    private Integer price;



}
