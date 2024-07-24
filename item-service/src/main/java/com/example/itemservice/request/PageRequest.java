package com.example.itemservice.request;

import lombok.Data;

@Data
public class PageRequest {

    int page;

    public PageRequest() {
        this.page = 1;
    }




}
