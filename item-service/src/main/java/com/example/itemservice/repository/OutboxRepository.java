package com.example.itemservice.repository;

import com.example.itemservice.domain.outbox.ItemOutbox;
import com.example.itemservice.domain.outbox.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxRepository extends JpaRepository<ItemOutbox,Long>  {

    @Query(
            value = "SELECT * FROM item_outbox WHERE status = :status LIMIT 100 FOR UPDATE SKIP LOCKED",
            nativeQuery = true
    )
    List<ItemOutbox> fetchBatchForProcessing(@Param("status") OutboxStatus status);

}
