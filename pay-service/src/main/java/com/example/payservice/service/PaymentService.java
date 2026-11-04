package com.example.payservice.service;

import com.example.payservice.client.EventServiceClient;
import com.example.payservice.client.ItemServiceClient;
import com.example.payservice.domain.PaymentDetail;
import com.example.payservice.domain.PaymentStatus;
import com.example.payservice.exception.PaymentException;
import com.example.payservice.messagequeue.KafkaProducer;
import com.example.payservice.repository.PaymentRepository;
import com.example.payservice.request.OrderRequest;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.example.payservice.response.ItemSnapshot;
import com.example.payservice.messagequeue.message.OrderCreatedEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final IamportClient iamportClient;
    private final ItemServiceClient itemService;
    private final KafkaProducer kafkaProducer;
    private final PaymentRepository paymentRepository;
    private final EventServiceClient eventServiceClient;


    public Payment info(String impUid) {
        try {

            return iamportClient.paymentByImpUid(impUid).getResponse();

        } catch (IamportResponseException e) {
            throw new PaymentException("결제 정보를 불러오지 못했습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public Payment valid(OrderRequest request) {


        
        try {
            Payment payment = iamportClient.paymentByImpUid(request.getImpUid()).getResponse();

            Map<String, Integer> quantities = new LinkedHashMap<>();
            if (request.getItems() == null || request.getItems().isEmpty()) {
                throw new PaymentException("주문 상품이 없습니다.");
            }
            request.getItems().forEach(item -> {
                if (item.getItemUUID() == null || item.getQuantity() <= 0) {
                    throw new PaymentException("올바르지 않은 주문 상품입니다.");
                }
                quantities.merge(item.getItemUUID(), item.getQuantity(), Math::addExact);
            });
            Map<String, ItemSnapshot> snapshots = itemService.snapshots(quantities).stream()
                    .collect(Collectors.toMap(ItemSnapshot::itemUUID, Function.identity()));
            List<OrderCreatedEvent.Item> items = quantities.entrySet().stream().map(entry -> {
                ItemSnapshot item = snapshots.get(entry.getKey());
                if (item == null || item.name() == null || item.itemPrice() == null || item.itemPrice() < 0) {
                    throw new PaymentException("상품 정보를 확인할 수 없습니다.");
                }
                return new OrderCreatedEvent.Item(item.itemUUID(), item.name(), entry.getValue(), item.itemPrice());
            }).toList();
            BigDecimal originalPrice = items.stream()
                    .map(item -> BigDecimal.valueOf(item.price()).multiply(BigDecimal.valueOf(item.quantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal discountPercent = BigDecimal.ZERO;
            BigDecimal hundred = new BigDecimal("100");

            if (request.getCouponId() != null) {

                try{
                    discountPercent = eventServiceClient.useCoupon(request.getCouponId(), request.getUserUUID());

                } catch (Exception e){
                    throw new RuntimeException("올바른 쿠폰이 아닙니다.");
                }
                
                
            }

            BigDecimal discountAmount = originalPrice.multiply(discountPercent)
                    .divide(hundred);  // 10000 * 10 / 100 = 1000

            BigDecimal finalPrice = originalPrice.subtract(discountAmount);
            


            if (finalPrice.compareTo(payment.getAmount()) != 0) {

                try {
                    iamportClient.cancelPaymentByImpUid(new CancelData(request.getImpUid(), true));
                } catch (IamportResponseException e) {
                    log.error("가격검증 결제 취소 실패");
                    throw new PaymentException("결제 취소에 실패했습니다.",e);
                }

                throw new PaymentException("결제 금액이 일치하지 않아 결제를 취소했습니다.");
            }

            try {
                kafkaProducer.send("order-create-topic", OrderCreatedEvent.from(request, items));
            } catch (KafkaException e) {
                try {
                    log.info("Kafka 메시지 생성 실패로 결제를 취소했습니다: impUid={}", request.getImpUid());
                    iamportClient.cancelPaymentByImpUid(new CancelData(request.getImpUid(), true));
                } catch (IamportResponseException ex) {
                    log.error("kafka 메시지 결제 취소 실패");
                    throw new PaymentException("결제 취소에 실패했습니다.",ex);
                }
                throw new PaymentException("주문 이벤트 발행에 실패하여 결제를 취소했습니다.");
            }

            paymentRepository.save(
                    PaymentDetail.builder()
                            .impUid(request.getImpUid())
                            .userUUID(request.getUserUUID())
                            .paymentStatus(PaymentStatus.PAID)
                            .amount(payment.getAmount())
                            .build());



            return payment;



        } catch (IamportResponseException e) {
            log.info("결제 정보 가져오기 실패");
            throw new PaymentException("결제 취소에 실패했습니다.",e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void cancel(OrderRequest request) {

        log.info("request = {}", request);

        try {

            iamportClient.cancelPaymentByImpUid(new CancelData(request.getImpUid(), true,
                    itemService.amount(request.getItems())));


        } catch (IamportResponseException e) {
            throw new PaymentException("결제 취소에 실패했습니다.",e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
