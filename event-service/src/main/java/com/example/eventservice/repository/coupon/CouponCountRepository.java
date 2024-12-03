package com.example.eventservice.repository.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponCountRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long increment(String key) {
        return redisTemplate
                .opsForValue()
                .increment(key);
    }


}
