package com.example.itemservice.response;

import lombok.Getter;

@Getter
public class ItemScoreResponse {

    private String itemUUID;

    private Double score;

    public ItemScoreResponse(String itemUUID, Double score) {
        this.itemUUID = itemUUID;
        this.score = score;
    }
}
