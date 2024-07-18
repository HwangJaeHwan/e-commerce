package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private ShoppingCart cart;

    private String itemUUID;

    private Integer quantity;


    public CartItem(ShoppingCart cart, String itemUUID, Integer quantity) {
        this.cart = cart;
        this.itemUUID = itemUUID;
        this.quantity = quantity;
    }


    public void updateCartItemQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
