package com.example.itemservice.response;

import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class ItemDetailResponse {


    private Long id;

    private String name;

    private String itemDescription;

    private Integer price;

    private Category category;

    private String itemUUID;

    private Integer stock;

    private List<UrlResponse> urls;

    private Double score;


    public ItemDetailResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.itemDescription = item.getItemDescription();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.category = item.getCategory();
        this.itemUUID = item.getItemUUID();
    }

    public void linkUrls(List<UrlResponse> urls) {
        this.urls = urls;
    }

    public void linkScore(Double score) {
        this.score = score;
    }
}
