package com.example.apigatewayservice.exception;

import io.jsonwebtoken.JwtException;

public class JwtExpiredException extends JwtException {

    private static final String MESSAGE = "토큰이 만료되었습니다.";

    public JwtExpiredException() {
        super(MESSAGE);
    }



}
