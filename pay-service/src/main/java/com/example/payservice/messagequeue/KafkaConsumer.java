package com.example.payservice.messagequeue;

import com.example.orderservice.domain.*;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.messagequeue.message.OrderMessage;
import com.example.orderservice.repository.CartItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ShoppingCartRepository;
import com.example.orderservice.request.ItemQuantity;
import com.example.orderservice.request.ItemRequest;
import com.example.orderservice.request.OrderRequest;
import com.example.payservice.domain.PaymentDetail;
import com.example.payservice.exception.PaymentException;
import com.example.payservice.messagequeue.message.PaymentCancel;
import com.example.payservice.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KafkaConsumer {


    private final ObjectMapper mapper;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    @KafkaListener(topics = "payment-cancel-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void consume(String message) {

        try {
            PaymentCancel paymentCancel = mapper.readValue(message, PaymentCancel.class);

            PaymentDetail detail = paymentRepository.findByImpUid(paymentCancel.getImpUid()).orElseThrow(() -> new PaymentException("해당 결제 정보를 찾을 수 없습니다."));
            try {

                iamportClient.cancelPaymentByImpUid(new CancelData(detail.getImpUid(), true,
                        detail.getAmount()));


            } catch (IamportResponseException e) {
                throw new PaymentException("결제 취소에 실패했습니다.",e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }  catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }


    }
}

