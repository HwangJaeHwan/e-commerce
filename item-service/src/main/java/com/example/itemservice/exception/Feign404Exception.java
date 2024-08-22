package com.example.itemservice.exception;

public class Feign404Exception extends ItemException{

    private static final String MESSAGE = "요청한 정보를 찾을 수 없습니다.";

    public Feign404Exception() {
        super(MESSAGE);
    }

    public Feign404Exception(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "404";
    }
}
