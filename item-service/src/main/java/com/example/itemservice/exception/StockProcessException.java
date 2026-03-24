package com.example.itemservice.exception;

public class StockProcessException extends ItemException {

    public StockProcessException(String message) {
        super(message);
    }

    @Override
    public String getStatusCode() {
        return "409";
    }
}
