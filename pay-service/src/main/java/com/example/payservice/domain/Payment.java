package com.example.payservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue
    private Long id;
    private String payUid;
    private String userUUID;
    private Integer amount;

    @Builder
    public Payment(String payUid, String userUUID, Integer amount) {
        this.payUid = payUid;
        this.userUUID = userUUID;
        this.amount = amount;
    }
}
