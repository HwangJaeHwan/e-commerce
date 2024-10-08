package com.example.apigatewayservice.jwt;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@Getter
@ConfigurationProperties(prefix = "token")
public class JwtKey {

    private byte[] key;

    public void setKey(String key) {
        this.key = Base64.getDecoder().decode(key);
    }


}
