package com.example.payservice.exception;

import com.siot.IamportRestClient.exception.IamportResponseException;

public class PaymentException extends RuntimeException {

    private int statusCode;
    private String message;

    public PaymentException(String message, IamportResponseException e) {
        super(message);
        statusCode = e.getHttpStatusCode();
    }


    public PaymentException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public String getStatusCode() {
        return String.valueOf(statusCode);
    }

}
