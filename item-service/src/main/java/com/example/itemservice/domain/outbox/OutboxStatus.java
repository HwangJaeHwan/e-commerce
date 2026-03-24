package com.example.itemservice.domain.outbox;

public enum OutboxStatus {
    PENDING,   // 아직 Kafka로 발행되지 않음
    SENT,      // 정상적으로 발행됨
    FAILED,    // 발행 실패 → 재시도 필요
}
