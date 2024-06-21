package com.example.itemservice.client;

import com.example.itemservice.response.UrlResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "image-service")
public interface ImageServiceClient {
    @GetMapping("/image-service/{UUID}")
    List<UrlResponse> getURls(@PathVariable String itemUUID);
}
