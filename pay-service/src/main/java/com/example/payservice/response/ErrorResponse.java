package com.example.payservice.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final String code;

    private final String message;

    private final Map<String, String> validations = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String name, String message) {
        validations.put(name, message);
    }

}
