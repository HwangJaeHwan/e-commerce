package com.example.reviewservice.exception;

public abstract class ReviewException extends RuntimeException {
    public ReviewException(String message) {
        super(message);
    }

    public ReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();
}
