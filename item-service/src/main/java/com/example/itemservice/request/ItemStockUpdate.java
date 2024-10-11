package com.example.itemservice.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemStockUpdate {

    private String orderUUID;

    private List<ItemStock> items;
}