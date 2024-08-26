package com.example.reviewservice.client;

import com.example.reviewservice.data.order.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/order-service/orders/uuid/{orderUUID}")
    OrderResponse getOrderByOrderUUID(@PathVariable String orderUUID);


}
