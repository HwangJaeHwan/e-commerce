package com.example.itemservice.exception;

public class UserTypeException extends ItemException{

    private static final String MESSAGE = "유저타입을 가져오지 못했습니다.";

    public UserTypeException() {
        super(MESSAGE);
    }

    public UserTypeException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
