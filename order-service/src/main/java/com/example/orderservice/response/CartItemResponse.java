package com.example.orderservice.response;

import com.example.orderservice.domain.CartItem;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CartItemResponse {

    private String name;
    private String itemUUID;
    private Integer quantity;
    private Integer price;
    private Integer itemPrice;


}
