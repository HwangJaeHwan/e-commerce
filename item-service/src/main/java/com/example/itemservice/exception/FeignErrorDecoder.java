package com.example.itemservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                if (methodKey.contains("getType")) {
                    return new UserTypeException();
                } else if (methodKey.contains("getReviews")) {
                    return new ReviewException();
                } else if (methodKey.contains("Score")) {
                    return new ScoreException();
                }
                break;
            case 404:
                return new Feign404Exception();
            default:
                return new FeignException();
        }
        return null;
    }
}
