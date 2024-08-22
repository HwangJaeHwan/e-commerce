package com.example.itemservice.client;

import com.example.itemservice.data.ImageType;
import com.example.itemservice.response.UrlResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "image-service")
public interface ImageServiceClient {
    @GetMapping("/image-service/{itemUUID}/urls")
    List<UrlResponse> getURls(@PathVariable String itemUUID, @RequestParam ImageType imageType);

    @DeleteMapping("/image-service/item/{itemUUID}")
    void deleteItemImage(@PathVariable String itemUUID);
}
