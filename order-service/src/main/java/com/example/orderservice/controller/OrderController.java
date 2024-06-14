package com.example.orderservice.controller;

import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.request.ItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @PostMapping("/{userId}/order")
    public void createOrder(@PathVariable String userId, List<ItemRequest> itemRequests) {




    }

    @GetMapping("/{userId}/orders")
    public void orders(@PathVariable String userId) {

    }
}
