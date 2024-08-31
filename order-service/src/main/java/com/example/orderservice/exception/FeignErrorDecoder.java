package com.example.orderservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();
        String message = "서버와 통신에 실패했습니다.";

        if (response.body() != null) {
            try (InputStream bodyIs = response.body().asInputStream()) {
                message = new String(bodyIs.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                status = 500;
                message =  "IO Exception 발생";
            }
        }

        return new FeignClientException(status, message);

    }
}
