package com.example.payservice.client;

import com.example.payservice.request.ItemQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "event-service")
public interface EventServiceClient {
    @PatchMapping("/coupon-service/{id}")
    BigDecimal useCoupon(@PathVariable("id") Long id, @RequestParam("userUUID") String userUUID);

}
