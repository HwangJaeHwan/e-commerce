package com.example.itemservice.domain.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OrderEventLog {


    @Id
    @Column(name = "order_event_log_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderUUID;

    private LocalDateTime createdAt = LocalDateTime.now();

    public OrderEventLog(String orderUUID) {
        this.orderUUID = orderUUID;
    }
}
