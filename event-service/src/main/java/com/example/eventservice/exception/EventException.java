package com.example.eventservice.exception;

public abstract class EventException extends RuntimeException {
    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();
}
