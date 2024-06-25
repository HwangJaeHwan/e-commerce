package com.example.imageservice.exception;

public abstract class ImageException extends RuntimeException {
    public ImageException(String message) {
        super(message);
    }

    public ImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();
}
