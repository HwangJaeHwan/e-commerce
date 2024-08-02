package com.example.orderservice.controller;

import com.example.orderservice.config.auth.UserInfo;
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

    @GetMapping("/orders")
    public PageResponse orders(UserInfo userInfo, @RequestParam(defaultValue = "1") int page) {

        return orderService.getOrdersByUserUUID(userInfo.getUuid(), page);
    }

    @PutMapping("/{orderUUID}")
    public void cancelOrder(UserInfo userInfo, @PathVariable String orderUUID) {

        orderService.cancel(userInfo.getUuid(), orderUUID);

    }

    @GetMapping("/cart/items")
    public List<CartItemResponse> getCartItems(UserInfo userInfo) {
        return cartService.getCartItems(userInfo.getUuid());
    }

}
