package com.example.itemservice.exception;

public class InsufficientStockException extends ItemException{

    private static final String MESSAGE = "재고가 부족합니다.";

    public InsufficientStockException() {
        super(MESSAGE);
    }

    public InsufficientStockException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "409";
    }
}
