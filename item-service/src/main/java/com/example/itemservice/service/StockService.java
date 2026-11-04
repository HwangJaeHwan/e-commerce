package com.example.itemservice.service;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.log.OrderEventLog;
import com.example.itemservice.domain.log.OrderEventLog.Operation;
import com.example.itemservice.domain.outbox.ItemOutbox;
import com.example.itemservice.exception.InsufficientStockException;
import com.example.itemservice.exception.ItemNotFoundException;
import com.example.itemservice.exception.StockProcessException;
import com.example.itemservice.messagequeue.message.OrderFailMessage;
import com.example.itemservice.repository.EventRepository;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.repository.OutboxRepository;
import com.example.itemservice.request.ItemStockUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@Service
public class StockService {
    private final ItemRepository items;
    private final EventRepository events;
    private final OutboxRepository outboxes;
    private final RedissonClient redis;
    private final ObjectMapper mapper;
    private final TransactionTemplate transaction;

    public StockService(ItemRepository items, EventRepository events, OutboxRepository outboxes,
                        RedissonClient redis, ObjectMapper mapper, PlatformTransactionManager manager) {
        this.items = items;
        this.events = events;
        this.outboxes = outboxes;
        this.redis = redis;
        this.mapper = mapper;
        transaction = new TransactionTemplate(manager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public void processStock(String message, boolean addition) {
        ItemStockUpdate update;
        try {
            update = mapper.readValue(message, ItemStockUpdate.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("메시지 파싱 실패", e);
        }
        if (update.getOrderUUID() == null || update.getOrderUUID().isBlank()
                || update.getItems() == null || update.getItems().isEmpty()) {
            throw new IllegalArgumentException("주문 UUID와 상품 목록은 필수입니다.");
        }
        Map<String, Integer> quantities = new TreeMap<>();
        update.getItems().forEach(item -> {
            if (item == null || item.getItemUUID() == null || item.getItemUUID().isBlank()
                    || item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("상품 UUID와 양수 수량은 필수입니다.");
            }
            quantities.merge(item.getItemUUID(), item.getQuantity(), Math::addExact);
        });

        List<RLock> acquired = new ArrayList<>();
        try {
            // Serialize duplicate events and reduction/restoration for the same order.
            acquire("lock:stock-order:" + update.getOrderUUID(), acquired);
            // Every order acquires product locks in the same order to avoid deadlocks.
            for (String uuid : quantities.keySet()) {
                acquire("lock:item:" + uuid, acquired);
            }
            try {
                transaction.executeWithoutResult(status -> apply(update.getOrderUUID(), quantities, addition));
            } catch (InsufficientStockException e) {
                // The stock transaction has already rolled back before failure persistence starts.
                transaction.executeWithoutResult(status -> recordFailure(update.getOrderUUID(), e.getMessage()));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락 대기 중 인터럽트 발생", e);
        } finally {
            // TransactionTemplate returns only after commit/rollback. Release only owned locks.
            for (int i = acquired.size() - 1; i >= 0; i--) {
                RLock lock = acquired.get(i);
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    private void acquire(String key, List<RLock> acquired) throws InterruptedException {
        RLock lock = redis.getLock(key);
        // No fixed lease: Redisson's watchdog renews the lock while processing continues.
        if (!lock.tryLock(5, TimeUnit.SECONDS)) {
            // Transient contention is retried by Kafka; it must not cancel an order.
            throw new StockProcessException("Lock Time Out: " + key);
        }
        acquired.add(lock);
    }

    private void apply(String orderUUID, Map<String, Integer> quantities, boolean addition) {
        Operation operation = addition ? Operation.RESTORE : Operation.REDUCE;
        if (events.findByOrderUUIDAndOperation(orderUUID, operation).isPresent()) {
            return;
        }
        if (addition) {
            OrderEventLog reduction = events.findByOrderUUIDAndOperation(orderUUID, Operation.REDUCE)
                    .orElseThrow(() -> new StockProcessException("재고 차감 이력이 없어 복구를 재시도합니다."));
            if (!reduction.isSuccessful()) {
                // A rolled-back reduction has nothing to restore.
                events.save(new OrderEventLog(orderUUID, operation, true));
                return;
            }
        }
        quantities.forEach((uuid, quantity) -> {
            Item item = items.findByItemUUID(uuid).orElseThrow(ItemNotFoundException::new);
            if (addition) {
                Math.addExact(item.getStock(), quantity);
            }
            item.updateQuantity(quantity, addition);
        });
        events.save(new OrderEventLog(orderUUID, operation, true));
    }

    private void recordFailure(String orderUUID, String reason) {
        if (events.findByOrderUUIDAndOperation(orderUUID, Operation.REDUCE).isPresent()) {
            return;
        }
        OrderFailMessage failure = new OrderFailMessage(orderUUID);
        failure.setMessage("Insufficient Stock: " + reason);
        try {
            outboxes.save(ItemOutbox.builder().eventType("ORDER-FAIL-STOCK")
                    .orderUUID(orderUUID).message(mapper.writeValueAsString(failure)).build());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("실패 이벤트 직렬화 실패", e);
        }
        events.save(new OrderEventLog(orderUUID, Operation.REDUCE, false));
    }
}
