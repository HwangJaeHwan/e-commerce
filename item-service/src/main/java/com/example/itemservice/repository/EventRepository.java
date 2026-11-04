package com.example.itemservice.repository;

import com.example.itemservice.domain.log.OrderEventLog;
import com.example.itemservice.domain.log.OrderEventLog.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EventRepository extends JpaRepository<OrderEventLog, Long> {
    Optional<OrderEventLog> findByOrderUUIDAndOperation(String orderUUID, Operation operation);
}
