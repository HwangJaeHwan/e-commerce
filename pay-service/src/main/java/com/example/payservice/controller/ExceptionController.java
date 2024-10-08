package com.example.payservice.controller;


import com.example.payservice.exception.PaymentException;
import com.example.payservice.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse validationException(MethodArgumentNotValidException e) {


        ErrorResponse error = ErrorResponse.builder()
                .code("400")
                .message("잘못된 입력입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            error.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return error;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> payException(PaymentException e) {

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(Integer.parseInt(e.getStatusCode())).body(body);

    }

}
