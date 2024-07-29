package com.example.orderservice.service;

import com.example.orderservice.client.ItemServiceClient;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderItem;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.exception.OrderCancellationDeniedException;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.exception.UnauthorizedException;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.message.OrderMessage;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.request.ItemQuantity;
import com.example.orderservice.request.ItemRequest;
import com.example.orderservice.request.OrderRequest;
import com.example.orderservice.response.ItemResponse;
import com.example.orderservice.response.OrderResponse;
import com.example.orderservice.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final ItemServiceClient itemServiceClient;
    private final KafkaProducer kafkaProducer;


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

        kafkaProducer.send("item-reduce-topic"
                , new OrderMessage(orderRequest.getItems().stream().map(ItemQuantity::new).toList()));
    }

    public PageResponse getOrdersByUserUUID(String userUUID,int page) {

        List<OrderResponse> orderResponses = new ArrayList<>();

        Page<Order> pageOrders = orderRepository.findAllByUserUUID(userUUID,PageRequest.of(page-1, 10));
        List<Order> orders = pageOrders.getContent();

        for (Order order : orders) {
            log.info("order = {}", order);
        }
        for (Order order : orders) {

            List<OrderItem> orderItems = order.getOrderItems();

            OrderResponse orderResponse = OrderResponse.builder()
                    .orderUUID(order.getOrderUUID())
                    .orderStatus(order.getOrderStatus())
                    .zipcode(order.getZipcode())
                    .city(order.getCity())
                    .street(order.getStreet())
                    .orderDate(order.getOrderDate())
                    .build();

            orderResponse.addItems(orderItems.stream().map(ItemResponse::new).toList());
            orderResponse.calTotalPrice(orderItems);

            orderResponses.add(orderResponse);

        }

        return PageResponse.builder()
                .isLast(pageOrders.isFirst())
                .isLast(pageOrders.isLast())
                .items(orderResponses)
                .totalElement(pageOrders.getTotalElements())
                .totalPage(pageOrders.getTotalPages())
                .build();

    }


    public void cancel(String userUUID, String orderUUID) {

        Order deleteOrder = orderRepository.findByOrderUUID(orderUUID).orElseThrow(OrderNotFoundException::new);

        if (!deleteOrder.getUserUUID().equals(userUUID)) {
            throw new UnauthorizedException();
        }

        if (!deleteOrder.getOrderStatus().equals(OrderStatus.ORDER)) {
            throw new OrderCancellationDeniedException();
        }

        deleteOrder.changeStatus(OrderStatus.CANCELLED);

        kafkaProducer.send("item-add-topic",
                new OrderMessage(deleteOrder.getOrderItems().stream().map(ItemQuantity::new).toList()));

    }


}
