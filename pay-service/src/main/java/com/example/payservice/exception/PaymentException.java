package com.example.payservice.exception;

import com.siot.IamportRestClient.exception.IamportResponseException;

public class PaymentException extends RuntimeException {

    private int statusCode;

    public PaymentException(IamportResponseException e) {
        super(e.getMessage());
        statusCode = e.getHttpStatusCode();
    }


    public PaymentException(String message) {
        super(message);
    }

    public String getStatusCode() {
        return String.valueOf(statusCode);
    }

}
