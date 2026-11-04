package com.example.itemservice.repository;

import com.example.itemservice.domain.outbox.ItemOutbox;
import com.example.itemservice.domain.outbox.OutboxStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<ItemOutbox, Long> {
    List<ItemOutbox> findTop100ByStatusOrderByIdAsc(OutboxStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from ItemOutbox o where o.id = :id")
    Optional<ItemOutbox> findForPublishing(@Param("id") Long id);
}
