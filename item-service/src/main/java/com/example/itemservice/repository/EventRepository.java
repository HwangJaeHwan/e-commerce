package com.example.itemservice.repository;

import com.example.itemservice.domain.log.OrderEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<OrderEventLog, Long> {


    boolean existsByOrderUUID(String orderUUID);

}
