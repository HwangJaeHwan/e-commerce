package com.example.itemservice.domain.item;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    @Lob
    private String itemDescription;

    private Integer price;

    private int stock;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String itemUUID;
    @Builder
    public Item(String name, String itemDescription, Integer price, int stock, Category category, String itemUUID) {
        this.name = name;
        this.itemDescription = itemDescription;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.itemUUID = itemUUID;
    }

    public void addQuantity(int quantity) {
        this.stock += quantity;
    }

    public void reduceQuantity(int quantity) {
        if (stock < quantity) {
            throw new RuntimeException("수량 부족");
        }

        this.stock -= quantity;
    }



}
