package com.example.orderservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                if (methodKey.contains("cartItemsInfo")) {
                    return new CartItemException();
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
