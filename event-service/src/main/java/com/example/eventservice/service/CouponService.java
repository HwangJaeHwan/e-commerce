package com.example.eventservice.service;

import com.example.eventservice.messagequeue.KafkaProducer;
import com.example.eventservice.repository.coupon.AppliedUserRepository;
import com.example.eventservice.repository.coupon.CouponCountRepository;
import com.example.eventservice.request.CouponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponCountRepository couponCountRepository;
    private final AppliedUserRepository appliedUserRepository;
    private final KafkaProducer kafkaProducer;

    public void add(CouponRequest request) {
        Long check = appliedUserRepository.add(request.getCouponKey(), request.getUserUUID());

        if (check != 1) {
            return;
        }

        long count = couponCountRepository.increment(request.getCouponKey());

        if (count > 100) {
            return;
        }


        kafkaProducer.send(request.getCouponKey(), request);

    }

}
