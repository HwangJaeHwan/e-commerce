package com.example.orderservice.exception;

public class OrderNotFoundException extends OrderException{

    private static final String MESSAGE = "해당하는 주문이 없습니다.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }

    public OrderNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "404";
    }
}
