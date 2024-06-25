package com.example.userservice.exception;

public class DuplicateEmailException extends UserException{

    private static final String MESSAGE = "이메일이 중복됩니다. ";

    public DuplicateEmailException() {
        super(MESSAGE);
    }

    public DuplicateEmailException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
