package com.example.reviewservice.exception;

public class ReviewNotFoundException extends ReviewException{

    private static final String MESSAGE = "리뷰를 찾을 수 없습니다.";

    public ReviewNotFoundException() {
        super(MESSAGE);
    }

    public ReviewNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "404";
    }
}
