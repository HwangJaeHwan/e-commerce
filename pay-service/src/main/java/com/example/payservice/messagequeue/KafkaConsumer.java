package com.example.payservice.messagequeue;

import com.example.payservice.config.kafka.KafkaConstraints;
import com.example.payservice.messagequeue.message.PaymentCancel;
import com.example.payservice.service.PaymentRefundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ObjectMapper mapper;
    private final PaymentRefundService refunds;

    @RetryableTopic(attempts = KafkaConstraints.MAX_ATTEMPT_COUNT,
            backoff = @Backoff(KafkaConstraints.BACK_OFF_PERIOD),
            listenerContainerFactory = "stringJsonKafkaListenerContainerFactory",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            dltTopicSuffix = ".DLT",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = "payment-cancel-topic", containerFactory = "stringJsonKafkaListenerContainerFactory")
    public void consume(String message) {
        try {
            refunds.refund(mapper.readValue(message, PaymentCancel.class));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("환불 이벤트 파싱 실패", e);
        }
    }

    @DltHandler
    public void consumeFromDLQ(String message) {
        // A DLT retains the original value; exception metadata is stored in Kafka headers.
        log.error("환불 재시도 소진. DLT 확인 및 재처리 필요. payload={}", message);
    }
}
