package com.example.orderservice.exception;

public class CartNotFoundException extends OrderException {
    private static final String MESSAGE = "장바구니에 담은 상품이 없습니다.";

    public CartNotFoundException() {
        super(MESSAGE);
    }

    public CartNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "404";
    }
}
