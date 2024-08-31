package com.example.reviewservice.exception;


public class FeignClientException extends ReviewException{

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
