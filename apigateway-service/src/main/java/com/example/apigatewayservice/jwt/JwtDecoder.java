package com.example.apigatewayservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtDecoder {


    private final JwtKey secret;

    public JwtResponse parseToken(String jwt) {
        Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getKey()))
                .build().parseSignedClaims(jwt).getPayload();

        return new JwtResponse(claims.getSubject(), claims.get("uuid", String.class));
    }




}
