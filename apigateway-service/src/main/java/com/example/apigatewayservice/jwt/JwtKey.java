package com.example.apigatewayservice.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.util.Base64;
@Data
@ConfigurationProperties(prefix = "token")
public class JwtKey {


    private SecretKey key;

}
