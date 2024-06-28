package com.example.userservice.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtProvider {


    public String createToken(Long id, String userUUID) {
        SecretKey key = Jwts.SIG.HS256.key().build();

        Claims claims = Jwts.claims().build();
        claims.put("uuid", userUUID);

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
