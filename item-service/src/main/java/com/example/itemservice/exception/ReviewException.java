package com.example.itemservice.exception;

public class ReviewException extends ItemException{

    private static final String MESSAGE = "리뷰 정보를 가져오지 못했습니다.";

    public ReviewException() {
        super(MESSAGE);
    }

    public ReviewException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
