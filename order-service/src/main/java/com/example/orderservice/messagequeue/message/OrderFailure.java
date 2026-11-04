package com.example.orderservice.messagequeue.message;

public record OrderFailure(String orderUUID, String message) {}
