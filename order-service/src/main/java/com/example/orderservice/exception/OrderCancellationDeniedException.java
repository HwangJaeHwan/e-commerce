package com.example.orderservice.exception;

public class OrderCancellationDeniedException extends OrderException{

    private static final String MESSAGE = "주문 취소가 불가능합니다.";

    public OrderCancellationDeniedException() {
        super(MESSAGE);
    }

    public OrderCancellationDeniedException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
