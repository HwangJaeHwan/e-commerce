package com.example.reviewservice.exception;

public class ReviewWriteException extends ReviewException{

    private static final String MESSAGE = "리뷰를 작성할 수 없는 상품입니다.";

    public ReviewWriteException() {
        super(MESSAGE);
    }

    public ReviewWriteException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
