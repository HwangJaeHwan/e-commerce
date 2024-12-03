package com.example.eventservice.messagequeue;

import com.example.eventservice.domain.Coupon;
import com.example.eventservice.repository.coupon.CouponRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KafkaConfig {

    private final CouponRepository couponRepository;


    @KafkaListener(topics = "first_coupon_create",groupId = "eventGroupId")
    public void createCoupon(String userUUID) {


        couponRepository.save(new Coupon(userUUID, 10));

    }

}
