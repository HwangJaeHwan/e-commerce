package com.example.eventservice.controller;

import com.example.eventservice.config.auth.UserInfo;
import com.example.eventservice.request.CouponRequest;
import com.example.eventservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon-service")
@Slf4j
@RequiredArgsConstructor
public class CouponController {


    private final CouponService couponService;


    @PostMapping("/add")
    public void addCoupon(UserInfo userInfo, @RequestBody CouponRequest couponRequest) {

        couponRequest.addUserUUID(userInfo.getUuid());
        couponService.add(couponRequest);

    }


}
