package com.example.orderservice.exception;


public class FeignClientException extends OrderException{

    private final int status;

    public FeignClientException(int status, String message) {
        super(message);
        this.status = status;
    }


    @Override
    public String getStatusCode() {
        return String.valueOf(status);
    }

}
