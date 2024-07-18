package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ShoppingCart {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    private String userUUID;

    public ShoppingCart(String userUUID) {
        this.userUUID = userUUID;
    }
}
