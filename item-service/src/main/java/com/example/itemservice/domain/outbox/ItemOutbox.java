package com.example.itemservice.domain.outbox;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String eventType;

    @Column(nullable = false ,unique = true)
    private String orderUUID;

    @Lob
    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    @Builder
    public ItemOutbox(String eventType,
                      String orderUUID,
                      String message) {
        this.eventType = eventType;
        this.orderUUID = orderUUID;
        this.message = message;
        this.status = OutboxStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void markPublished() {
        this.status = OutboxStatus.SENT;
        this.updatedAt = LocalDateTime.now();
        this.publishedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = OutboxStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }
}
