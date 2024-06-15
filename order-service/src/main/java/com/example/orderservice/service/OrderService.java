package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderItem;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.request.ItemRequest;
import com.example.orderservice.request.OrderRequest;
import com.example.orderservice.response.ItemResponse;
import com.example.orderservice.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    public void createOrder(OrderRequest orderRequest) {


        Order order = Order.builder()
                .orderStatus(OrderStatus.ORDER)
                .userUUID(orderRequest.getUserUUID())
                .orderUUID(UUID.randomUUID().toString())
                .orderDate(LocalDateTime.now())
                .city(orderRequest.getCity())
                .street(orderRequest.getStreet())
                .zipcode(orderRequest.getZipcode())
                .build();

        for (ItemRequest item : orderRequest.getItems()) {
            order.addItem(
                    OrderItem.builder()
                            .itemUUID(item.getItemUUID())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build()
            );
        }

        orderRepository.save(order);
    }

    public void getOrdersByUserUUID(String userUUID) {

        List<OrderResponse> orderResponses = new ArrayList<>();

        List<Order> orders = orderRepository.findAllByUserUUID(userUUID);
        for (Order order : orders) {

            List<ItemResponse> list = order.getOrderItems().stream().map(ItemResponse::new).toList();

            OrderResponse.builder()
                    .orderUUID(order.getOrderUUID())
                    .userUUID(order.getUserUUID())
                    .orderDate(order.getOrderDate())
                    .build();

        }

    }


}
