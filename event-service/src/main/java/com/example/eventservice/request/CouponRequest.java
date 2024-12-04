package com.example.eventservice.request;

import lombok.Getter;

@Getter
public class CouponRequest {


    private String userUUID;

    private String couponKey;

    private String name;

    private Integer percent;


    public void addUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
}
