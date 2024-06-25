package com.example.itemservice.exception;

public class ItemNotFoundException extends ItemException{

    private static final String MESSAGE = "상품이 없습니다.";

    public ItemNotFoundException() {
        super(MESSAGE);
    }

    public ItemNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "404";
    }
}
