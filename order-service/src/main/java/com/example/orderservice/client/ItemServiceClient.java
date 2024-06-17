package com.example.orderservice.client;

import com.example.orderservice.request.ItemQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "item-service")
public interface ItemServiceClient {
    @PatchMapping("/quantity/add")
    void addQuantity(@RequestBody List<ItemQuantity> quantities);

    @PatchMapping("/quantity/reduce")
    void reduceQuantity(@RequestBody List<ItemQuantity> quantities);


}
