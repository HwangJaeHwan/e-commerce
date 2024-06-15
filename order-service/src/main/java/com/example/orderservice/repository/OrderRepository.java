package com.example.orderservice.repository;

import com.example.orderservice.domain.Order;
import com.example.orderservice.response.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findAllByUserUUID(String userUUID);

}
