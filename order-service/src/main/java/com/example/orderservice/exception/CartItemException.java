package com.example.orderservice.exception;

public class CartItemException extends OrderException {

    private static final String MESSAGE = "장바구니 정보를 불러오지 못했습니다.";

    public CartItemException() {
        super(MESSAGE);
    }

    public CartItemException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
