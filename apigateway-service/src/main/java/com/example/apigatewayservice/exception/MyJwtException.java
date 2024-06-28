package com.example.apigatewayservice.exception;

import io.jsonwebtoken.JwtException;

public class MyJwtException extends JwtException {

    private static final String MESSAGE = "JWT가 유효하지 않습니다. ";

    public MyJwtException() {
        super(MESSAGE);
    }



}
