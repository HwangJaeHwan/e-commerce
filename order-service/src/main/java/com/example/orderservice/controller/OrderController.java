package com.example.orderservice.controller;

import com.example.orderservice.request.OrderRequest;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderRequest orderRequest) {

        orderService.createOrder(orderRequest);

    }

    @GetMapping("/{userUUID}/orders")
    public List<OrderResponse> orders(@PathVariable String userUUID) {
        return orderService.getOrdersByUserUUID(userUUID);
    }

    @PutMapping("/{userUUID}/{orderUUID}")
    public void cancelOrder(@PathVariable String userUUID,@PathVariable String orderUUID) {

        orderService.cancel(userUUID,orderUUID);

    }

}
