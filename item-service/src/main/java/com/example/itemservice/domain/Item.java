package com.example.itemservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String name;

    @NotBlank
    @Lob
    private String itemDescription;

    @NotNull
    private Integer price;


    public Item(String name, String itemDescription, Integer price) {
        this.name = name;
        this.itemDescription = itemDescription;
        this.price = price;
    }
}
