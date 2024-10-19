package com.example.payservice.messagequeue;


import com.example.payservice.config.kafka.KafkaConstraints;
import com.example.payservice.domain.PaymentDetail;
import com.example.payservice.exception.PaymentException;
import com.example.payservice.messagequeue.message.PaymentCancel;
import com.example.payservice.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper mapper;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;



    @RetryableTopic(
            attempts = KafkaConstraints.MAX_ATTEMPT_COUNT,
            backoff = @Backoff(KafkaConstraints.BACK_OFF_PERIOD),
            listenerContainerFactory = "stringJsonKafkaListenerContainerFactory",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = "payment-cancel-topic",containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void consume(@Payload PaymentCancel paymentCancel) {

        try {
            PaymentDetail detail = paymentRepository.findByImpUid(paymentCancel.getImpUid())
                    .orElseThrow(() -> new PaymentException("해당 결제 정보를 찾을 수 없습니다."));

            iamportClient.cancelPaymentByImpUid(new CancelData(detail.getImpUid(), true, detail.getAmount()));
            log.info("결제 취소 성공: {}", paymentCancel.getImpUid());

        } catch (IamportResponseException e) {
            log.error("결제 취소 실패 - IamportResponseException: {}", e.getMessage());
            throw new PaymentException("결제 취소에 실패했습니다.", e);
        } catch (IOException e) {
            log.error("결제 취소 실패 - IOException: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (PaymentException e) {
            log.error("해당 결제 정보를 찾을 수 없습니다. impUid = {}", paymentCancel.getImpUid());
        }
    }


    @KafkaListener(topics = "payment-cancel-topic.DLT", containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void consumeFromDLQ(String message) {
        try {
            JsonNode jsonNode = mapper.readTree(message);

            String exceptionMessage = jsonNode.get("exception").get("message").asText();
            String impUid = jsonNode.get("value").get("impUid").asText();

            log.error("DLQ 메시지 - 예외 메시지: {}, impUid: {}", exceptionMessage, impUid);

            paymentRepository.findByImpUid(impUid).ifPresent(p -> log.info("DLQ 메시지 - userUUID: {}", p.getUserUUID()));


        } catch (JsonProcessingException e) {
            log.error("DLQ 메시지 파싱 오류: {}", e.getMessage());
        }
    }

}

