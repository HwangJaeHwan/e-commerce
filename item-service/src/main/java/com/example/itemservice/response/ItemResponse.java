package com.example.itemservice.response;

import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ItemResponse {


    private Long id;

    private String name;

    private String itemDescription;

    private Integer price;

    private Category category;

    private String itemUUID;


    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.itemDescription = item.getItemDescription();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.itemUUID = item.getItemUUID();
    }
}
