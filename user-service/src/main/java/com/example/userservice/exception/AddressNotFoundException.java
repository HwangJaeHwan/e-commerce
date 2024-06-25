package com.example.userservice.exception;

public class AddressNotFoundException extends UserException{

    private static final String MESSAGE = "주소록을 찾을 수 없습니다.";

    public AddressNotFoundException() {
        super(MESSAGE);
    }

    public AddressNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
