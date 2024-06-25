package com.example.userservice.exception;

public class PasswordDiffException extends UserException{

    private static final String MESSAGE = "아이디나 비밀번호가 다릅니다. ";

    public PasswordDiffException() {
        super(MESSAGE);
    }

    public PasswordDiffException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
