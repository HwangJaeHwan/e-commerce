package com.example.imageservice.exception;

public class UnauthorizedException extends ImageException{

    private static final String MESSAGE = "권한이 없습니다.";

    public UnauthorizedException() {
        super(MESSAGE);
    }

    public UnauthorizedException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "401";
    }
}
