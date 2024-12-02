package com.example.eventservice.service;

import com.example.eventservice.domain.Coupon;
import com.example.eventservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final CouponRepository couponRepository;



    public void add(String userUUID) {

        long count = couponRepository.count();

        if (count > 100) {
            return;
        }


        couponRepository.save(new Coupon(userUUID));

    }

}
