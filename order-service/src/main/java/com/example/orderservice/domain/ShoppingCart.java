package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ShoppingCart {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    private String userUUID;

    private String itemUUID;

    private Integer quantity;


    public ShoppingCart(String userUUID, String itemUUID, Integer quantity) {
        this.userUUID = userUUID;
        this.itemUUID = itemUUID;
        this.quantity = quantity;
    }
}
