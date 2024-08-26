package com.example.reviewservice.data.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemResponse {

    private String itemUUID;
    private int quantity;
    private int price;


}
