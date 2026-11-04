package com.example.itemservice.domain.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "order_event_log", uniqueConstraints = @UniqueConstraint(name = "uk_stock_order_operation", columnNames = {"order_uuid", "operation"}))
public class OrderEventLog {
    public enum Operation { REDUCE, RESTORE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_event_log_id")
    private Long id;

    @Column(name = "order_uuid", nullable = false)
    private String orderUUID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Operation operation;

    @Column(nullable = false)
    private boolean successful;

    private LocalDateTime createdAt = LocalDateTime.now();

    public OrderEventLog(String orderUUID, Operation operation, boolean successful) {
        this.orderUUID = orderUUID;
        this.operation = operation;
        this.successful = successful;
    }
}
