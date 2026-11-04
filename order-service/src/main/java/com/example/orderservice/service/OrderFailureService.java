package com.example.orderservice.service;

import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.domain.outbox.OrderOutbox;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.messagequeue.message.OrderFailure;
import com.example.orderservice.messagequeue.message.PaymentCancel;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFailureService {
    private final OrderRepository orders;
    private final OrderOutboxRepository outboxes;
    private final ObjectMapper mapper;

    @Transactional
    public void cancel(OrderFailure failure) {
        if (failure == null || failure.orderUUID() == null || failure.orderUUID().isBlank()) {
            throw new IllegalArgumentException("주문 UUID는 필수입니다.");
        }
        var order = orders.findForCancellation(failure.orderUUID()).orElseThrow(OrderNotFoundException::new);
        if (outboxes.existsByOrderUUID(order.getOrderUUID())) return;
        if (order.getImpUid() == null || order.getImpUid().isBlank()) {
            throw new IllegalStateException("환불할 결제 ID가 없습니다.");
        }
        try {
            String message = mapper.writeValueAsString(new PaymentCancel(order.getImpUid(), failure.message()));
            outboxes.save(new OrderOutbox(order.getOrderUUID(), message));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("환불 요청 직렬화 실패", e);
        }
        order.changeStatus(OrderStatus.CANCELLED);
        order.setCancelReason(failure.message());
    }
}
