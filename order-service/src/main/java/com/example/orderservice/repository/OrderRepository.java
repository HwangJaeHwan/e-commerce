package com.example.orderservice.repository;

import com.example.orderservice.domain.Order;
import com.example.orderservice.response.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findByOrderUUID(String orderUUID);

    @Query("select distinct o from Order o join fetch o.orderItems where o.userUUID = :userUUID")
    List<Order> findAllByUserUUID(@Param("userUUID") String userUUID);


}
