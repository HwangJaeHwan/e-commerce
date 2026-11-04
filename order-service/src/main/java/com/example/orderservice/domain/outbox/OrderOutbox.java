package com.example.orderservice.domain.outbox;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OrderOutbox {
    public enum Status { PENDING, SENT, FAILED }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderUUID;
    @Lob @Column(nullable = false)
    private String message;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Status status = Status.PENDING;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    public OrderOutbox(String orderUUID, String message) {
        this.orderUUID = orderUUID;
        this.message = message;
    }

    public void markPublished() {
        status = Status.SENT;
        publishedAt = updatedAt = LocalDateTime.now();
    }

    public void markFailed() {
        status = Status.FAILED;
        updatedAt = LocalDateTime.now();
    }
}
