package com.example.orderservice.controller;

import com.example.orderservice.request.OrderRequest;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderRequest orderRequest) {

        orderService.createOrder(orderRequest);

    }

    @GetMapping("/{userUUID}/orders")
    public void orders(@PathVariable String userUUID) {
        orderService.getOrdersByUserUUID(userUUID);
    }
}
