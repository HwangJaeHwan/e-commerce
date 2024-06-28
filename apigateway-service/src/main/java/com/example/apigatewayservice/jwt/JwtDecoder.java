package com.example.apigatewayservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {


    private final JwtKey key;

    public JwtResponse parseToken(String jwt) {
        Claims claims = Jwts.parser().verifyWith(key.getKey()).build().parseSignedClaims(jwt).getPayload();
        return new JwtResponse(claims.get("uuid", String.class), claims.getSubject());
    }




}
