package com.example.reviewservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "image-service")
public interface ImageServiceClient {

    @DeleteMapping("/image-service/review/{reviewUUID}")
    void deleteReviewImage(@PathVariable String reviewUUID);
}
