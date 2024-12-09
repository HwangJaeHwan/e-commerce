package com.example.eventservice.response;

import com.example.eventservice.domain.Coupon;
import lombok.Getter;

@Getter
public class CouponListResponse {

    private Long id;

    private String name;

    private int percent;

    public CouponListResponse(Coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.percent = coupon.getPercent();
    }
}
