package com.example.apigatewayservice.filter;

import com.example.apigatewayservice.jwt.JwtDecoder;
import com.example.apigatewayservice.jwt.JwtResponse;
import com.example.apigatewayservice.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final JwtDecoder jwtDecoder;
    private final ObjectMapper objectMapper;
    @Autowired
    public AuthFilter(JwtDecoder jwtDecoder, ObjectMapper objectMapper) {
        super(Config.class);
        this.jwtDecoder = jwtDecoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {


        return (exchange, chain) -> {
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.info("authFilter = {}", authorizationHeader);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                JwtResponse jwtResponse = null;

                try {
                    jwtResponse = jwtDecoder.parseToken(jwt);
                    log.info("response.uuid={}",jwtResponse.getUuid());
                    log.info("response.id={}",jwtResponse.getId());
                } catch (JwtException e) {
                    return onError(exchange,new ErrorResponse(e.getMessage()));
                }

                addHeaders(exchange, jwtResponse);

            }
            return chain.filter(exchange);
        };





    }

    private static void addHeaders(ServerWebExchange exchange, JwtResponse jwtResponse) {
        exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    httpHeaders.add("id", jwtResponse.getId());
                    httpHeaders.add("uuid", jwtResponse.getUuid());
                })
                .build();
    }

    private Mono<Void> onError(ServerWebExchange exchange, ErrorResponse error){
        ServerHttpResponse response = exchange.getResponse();

        byte[] bytes = convertBytes(error);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setRawStatusCode(error.getCode());

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    private byte[] convertBytes(ErrorResponse error)  {
        try {
            return objectMapper.writeValueAsBytes(error);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Config {
    }

}


