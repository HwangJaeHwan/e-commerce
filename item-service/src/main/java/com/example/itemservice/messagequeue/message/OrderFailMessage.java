package com.example.itemservice.messagequeue.message;

import lombok.Getter;

@Getter
public class OrderFailMessage {

    private final String orderUUID;

    private String message;

    public OrderFailMessage(String orderUUID) {
        this.orderUUID = orderUUID;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
