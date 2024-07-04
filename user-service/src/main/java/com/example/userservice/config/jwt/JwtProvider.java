package com.example.userservice.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;



    public String createToken(Long id, String userUUID) {
        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getKey());

        Claims claims = Jwts.claims().add("uuid", userUUID).build();

        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(id))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + 3600000))
                .signWith(key)
                .compact();
    }


}
