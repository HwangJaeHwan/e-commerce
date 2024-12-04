package com.example.eventservice.messagequeue;

import com.example.eventservice.domain.Coupon;
import com.example.eventservice.repository.coupon.CouponRepository;
import com.example.eventservice.request.CouponRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CouponRepository couponRepository;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "coupon_create", groupId = "eventGroupId")
    public void createCoupon(String message) {

        try {

            CouponRequest couponRequest = mapper.readValue(message, CouponRequest.class);

            couponRepository.save(new Coupon(couponRequest));

        } catch (JsonProcessingException e) {
            // JSON 변환 오류 처리
            throw new RuntimeException(e);
        }

    }

}
