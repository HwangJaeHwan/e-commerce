package com.example.orderservice.exception;

public class FeignException extends OrderException {

    private static final String MESSAGE = "서버 통신 오류";

    public FeignException() {
        super(MESSAGE);
    }

    public FeignException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "500";
    }
}
