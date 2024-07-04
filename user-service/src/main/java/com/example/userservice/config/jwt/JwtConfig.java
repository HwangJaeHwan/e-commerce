package com.example.userservice.config.jwt;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;
@Getter
@ConfigurationProperties("token")
public class JwtConfig {

    private byte[] key;

    public void setKey(String key) {
        this.key = Base64.getDecoder().decode(key);
    }
}
