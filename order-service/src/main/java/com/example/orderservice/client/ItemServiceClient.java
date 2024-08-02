package com.example.orderservice.client;

import com.example.orderservice.request.ItemQuantity;
import com.example.orderservice.response.CartItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "item-service")
public interface ItemServiceClient {
    @PostMapping("/item-service/items/info")
    List<CartItemResponse> cartItemsInfo(@RequestBody Map<String,Integer> infos);


}
