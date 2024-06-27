package com.example.userservice.config.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


@Component
public class JwtProvider {


    public String createToken(String userUUID) {
        SecretKey key = Jwts.SIG.HS256.key().build();

        return Jwts.builder()
                .subject(userUUID)
                .signWith(key)
                .compact();
    }


}
