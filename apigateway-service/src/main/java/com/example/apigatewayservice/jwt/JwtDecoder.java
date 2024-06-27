package com.example.apigatewayservice.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {


    private final JwtKey key;

    public String parseToken(String jwt) {
        return Jwts.parser().verifyWith(key.getKey()).build().parseSignedClaims(jwt).getPayload().getSubject();
    }




}
