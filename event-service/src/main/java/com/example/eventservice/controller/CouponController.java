package com.example.eventservice.controller;

import com.example.eventservice.config.auth.UserInfo;
import com.example.eventservice.request.CouponRequest;
import com.example.eventservice.response.CouponListResponse;
import com.example.eventservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping("/list")
    public List<CouponListResponse> getList(UserInfo userInfo) {
        return couponService.getList(userInfo.getUuid());
    }

    @PatchMapping("/{id}")
    public BigDecimal useCoupon(@PathVariable Long id, @RequestParam String userUUID) {
        return couponService.useCoupon(id, userUUID);
    }
}
