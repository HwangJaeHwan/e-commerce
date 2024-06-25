package com.example.userservice.exception;

public class DuplicateLoginIdException extends UserException{

    private static final String MESSAGE = "아이디가 중복됩니다. ";

    public DuplicateLoginIdException() {
        super(MESSAGE);
    }

    public DuplicateLoginIdException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
