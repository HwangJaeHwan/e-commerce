package com.example.itemservice.exception;

public class ScoreException extends ItemException{

    private static final String MESSAGE = "평점 정보를 가져오지 못했습니다.";

    public ScoreException() {
        super(MESSAGE);
    }

    public ScoreException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
