package com.example.userservice.request;

import lombok.Getter;

@Getter
public class CartRequest {

    private String itemUUID;
    private Integer quantity;

    public CartRequest(String itemUUID, Integer quantity) {
        this.itemUUID = itemUUID;
        this.quantity = quantity;
    }
}
