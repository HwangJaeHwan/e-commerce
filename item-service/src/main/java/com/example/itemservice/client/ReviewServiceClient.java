package com.example.itemservice.client;

import com.example.itemservice.response.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "review-service")
public interface ReviewServiceClient {
    @GetMapping("/review-service/{itemUUID}")
    List<ReviewResponse> getReviews(@PathVariable String itemUUID);

    @GetMapping("/review-service/{itemUUID}/score")
    Double getAverageScore(@PathVariable String itemUUID);
}
