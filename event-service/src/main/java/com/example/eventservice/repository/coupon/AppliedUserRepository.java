package com.example.eventservice.repository.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppliedUserRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long add(String key,String userUUID) {
        return redisTemplate
                .opsForSet()
                .add(key+"_set",userUUID);
    }
}
