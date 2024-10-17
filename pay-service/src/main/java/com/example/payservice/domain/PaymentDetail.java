package com.example.payservice.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class PaymentDetail {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String impUid;
    private String userUUID;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String reason;

    @Builder
    public PaymentDetail(String impUid, String userUUID, PaymentStatus paymentStatus, BigDecimal amount) {
        this.impUid = impUid;
        this.userUUID = userUUID;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public void changeStatus() {

        this.paymentStatus = PaymentStatus.CANCEL;

    }
}
