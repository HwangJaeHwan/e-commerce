package com.example.itemservice.exception;

public abstract class ItemException extends RuntimeException {
    public ItemException(String message) {
        super(message);
    }

    public ItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();
}
