package com.example.payservice.messagequeue.message;

import lombok.Getter;

@Getter
public class PaymentCancel {

    private String ImpUid;

    private String reason;



}
