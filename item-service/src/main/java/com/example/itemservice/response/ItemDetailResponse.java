package com.example.itemservice.response;

import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import lombok.Getter;

@Getter
public class ItemDetailResponse {


    private Long id;

    private String name;

    private String itemDescription;

    private Integer price;

    private Category category;

    private String itemUUID;


    public ItemDetailResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.itemDescription = item.getItemDescription();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.itemUUID = item.getItemUUID();
    }
}
