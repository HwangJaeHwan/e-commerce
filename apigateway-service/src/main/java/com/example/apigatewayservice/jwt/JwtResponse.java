package com.example.apigatewayservice.jwt;

import lombok.Getter;

@Getter
public class JwtResponse {

    private String id;

    private String uuid;

    public JwtResponse(String id, String uuid) {
        this.id = id;
        this.uuid = uuid;
    }
}
