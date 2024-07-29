package com.example.orderservice.controller;

import com.example.orderservice.domain.CartItem;
import com.example.orderservice.request.OrderRequest;
import com.example.orderservice.response.CartItemResponse;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.response.PageResponse;
import com.example.orderservice.service.CartService;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderRequest orderRequest) {

        orderService.createOrder(orderRequest);

    }

    @GetMapping("/{userUUID}/orders")
    public PageResponse orders(@PathVariable String userUUID, @RequestParam(defaultValue = "1") int page) {

        return orderService.getOrdersByUserUUID(userUUID, page);
    }

    @PutMapping("/{userUUID}/{orderUUID}")
    public void cancelOrder(@PathVariable String userUUID, @PathVariable String orderUUID) {

        orderService.cancel(userUUID, orderUUID);

    }

    @GetMapping("/cart/{userUUID}")
    public List<CartItemResponse> getCartItems(@PathVariable String userUUID) {
        return cartService.getCartItems(userUUID);
    }

}
