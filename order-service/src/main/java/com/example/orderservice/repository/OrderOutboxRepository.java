package com.example.orderservice.repository;

import com.example.orderservice.domain.outbox.OrderOutbox;
import com.example.orderservice.domain.outbox.OrderOutbox.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long> {
    boolean existsByOrderUUID(String orderUUID);
    List<OrderOutbox> findTop100ByStatusOrderByIdAsc(Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderOutbox o where o.id = :id")
    Optional<OrderOutbox> findForPublishing(@Param("id") Long id);
}
