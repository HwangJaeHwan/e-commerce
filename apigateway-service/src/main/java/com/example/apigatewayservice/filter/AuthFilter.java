package com.example.apigatewayservice.filter;

import com.example.apigatewayservice.jwt.JwtDecoder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter> {

    private final JwtDecoder jwtDecoder;

    @Override
    public GatewayFilter apply(AuthFilter config) {

        //custom pre filter
        return (exchange, chain) -> {
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                String uuid = null;

                try {
                    uuid = jwtDecoder.parseToken(jwt);
                } catch (Exception e) {
                    return onError(exchange, "error", HttpStatus.UNAUTHORIZED);
                }

                exchange.getRequest().mutate().header("uuid", uuid).build();
            }
            return chain.filter(exchange);
        };





    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(httpStatus);
        log.error(error);

        return response.setComplete();
    }

}


