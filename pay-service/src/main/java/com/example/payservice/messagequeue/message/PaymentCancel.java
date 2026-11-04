package com.example.payservice.messagequeue.message;

import lombok.Getter;

@Getter
public class PaymentCancel {

    private String impUid;

    private String reason;



}
