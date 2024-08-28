package com.example.orderservice.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class OrderCancelRequest {

    private String impUid;
    private List<CancelItem> items = new ArrayList<>();

    public OrderCancelRequest(String impUid) {
        this.impUid = impUid;
    }
}
