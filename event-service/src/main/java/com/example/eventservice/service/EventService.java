package com.example.eventservice.service;

import com.example.eventservice.domain.Coupon;
import com.example.eventservice.messagequeue.KafkaProducer;
import com.example.eventservice.repository.coupon.CouponCountRepository;
import com.example.eventservice.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final KafkaProducer kafkaProducer;

    public void add(String key,String userUUID) {

        long count = couponCountRepository.increment(key);

        if (count > 100) {
            return;
        }


        kafkaProducer.send(key, userUUID);

    }

}
