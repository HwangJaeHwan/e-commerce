package com.example.itemservice.response;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.request.CartItemInfo;
import lombok.Getter;

@Getter
public class CartItemResponse {

    private String name;
    private String itemUUID;
    private Integer price;
    private Integer quantity;
    private Integer itemPrice;

    public CartItemResponse(Item item) {
        this.name = item.getName();
        this.itemUUID = item.getItemUUID();
        this.itemPrice = item.getPrice();
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void calItemPrice() {
        this.price = itemPrice * quantity;
    }
}
