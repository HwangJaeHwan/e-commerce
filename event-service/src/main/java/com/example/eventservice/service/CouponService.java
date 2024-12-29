package com.example.eventservice.service;

import com.example.eventservice.domain.Coupon;
import com.example.eventservice.messagequeue.KafkaProducer;
import com.example.eventservice.repository.coupon.AppliedUserRepository;
import com.example.eventservice.repository.coupon.CouponRedisRepository;
import com.example.eventservice.repository.coupon.CouponRepository;
import com.example.eventservice.request.CouponRequest;
import com.example.eventservice.response.CouponListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRedisRepository couponRedisRepository;
    private final AppliedUserRepository appliedUserRepository;
    private final CouponRepository couponRepository;
    private final KafkaProducer kafkaProducer;
    private final RedisTemplate<String, String> redisTemplate;

    public void add(CouponRequest request) {
        Long check = appliedUserRepository.add(request.getCouponKey(), request.getUserUUID());

        if (check != 1) {
            return;
        }

        long count = couponRedisRepository.increment(request.getCouponKey());

        if (count > 100) {
            return;
        }


        kafkaProducer.send(request.getCouponKey(), request);

    }

    public List<CouponListResponse> getList(String userUUID) {

        List<Coupon> coupons = couponRepository.getCoupons(userUUID);

        return coupons.stream().map(CouponListResponse::new).toList();


    }


    public BigDecimal useCoupon(Long id, String userUUID) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(RuntimeException::new);

        Boolean lock = redisTemplate.opsForValue().setIfAbsent(String.valueOf(id), "COUPON:LOCK",
                Duration.ofMillis(3_000)
        );

        if (Boolean.TRUE.equals(lock)) {
            try {
                if (!coupon.getUserUUID().equals(userUUID)) {
                    throw new RuntimeException("쿠폰의 주인이 아님");
                }

                if (coupon.isUsed()) {
                    throw new RuntimeException("이미 사용된 쿠폰");
                }

                coupon.use();
            } finally {
                redisTemplate.delete(String.valueOf(id));
            }
        } else {
            throw new RuntimeException("쿠폰 사용 중입니다. 나중에 다시 시도해주세요.");
        }

        return new BigDecimal(coupon.getPercent());



    }

}

