package com.example.apigatewayservice.response;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private Integer code;

    private String message;

    public ErrorResponse(String message) {
        this.code = 401;
        this.message = message;
    }

    public ErrorResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
