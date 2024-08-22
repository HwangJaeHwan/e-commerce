package com.example.itemservice.exception;

public class FeignException extends ItemException{

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
