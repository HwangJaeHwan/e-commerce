package com.example.orderservice.request;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class ItemRequest {

    private Long itemId;

    private String name;

    private String itemUUID;

    private int quantity;

    private Integer price;



}
