package com.example.userservice.exception;

public class PasswordCheckException extends UserException{

    private static final String MESSAGE = "비밀번호와 비밀번호 확인이 다릅니다. ";

    public PasswordCheckException() {
        super(MESSAGE);
    }

    public PasswordCheckException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "400";
    }
}
