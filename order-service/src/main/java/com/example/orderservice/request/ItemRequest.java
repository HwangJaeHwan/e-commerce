package com.example.orderservice.request;

import lombok.Getter;


@Getter
public class ItemRequest {

    private String name;

    private String itemUUID;

    private int quantity;

    private Integer price;



}
