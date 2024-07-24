package com.example.itemservice.client;

import com.example.itemservice.response.ItemScoreResponse;
import com.example.itemservice.response.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "review-service")
public interface ReviewServiceClient {
    @GetMapping("/review-service/{itemUUID}")
    List<ReviewResponse> getReviews(@PathVariable String itemUUID);

    @GetMapping("/review-service/{itemUUID}/score")
    Double getAverageScore(@PathVariable String itemUUID);

    @PostMapping("/review-service/scores")
    Map<String,Double> getAverageScores(@RequestBody List<String> itemUUIDs);
}
