package com.example.orderservice.client;

import com.example.orderservice.data.OrderCancelRequest;
import com.example.orderservice.response.CartItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "pay-service")
public interface PayServiceClient {
    @PostMapping("/pay-service/cancel")
    List<CartItemResponse> cartItemsInfo(@RequestBody OrderCancelRequest request);



}
