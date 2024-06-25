package com.example.orderservice.exception;

public abstract class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();
}
