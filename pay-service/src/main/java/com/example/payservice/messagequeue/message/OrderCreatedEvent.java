package com.example.payservice.messagequeue.message;

import com.example.payservice.request.OrderRequest;
import java.util.List;

public record OrderCreatedEvent(String orderUUID, String impUid, String userUUID,
        String address, String name, String detailAddress, String zipcode,
        String phoneNumber, Boolean fromCart, List<Item> items) {
    public record Item(String itemUUID, String name, int quantity, int price) {}

    public static OrderCreatedEvent from(OrderRequest request, List<Item> items) {
        // Retries for the same payment retain the same order identity.
        return new OrderCreatedEvent(request.getImpUid(), request.getImpUid(), request.getUserUUID(),
                request.getAddress(), request.getName(), request.getDetailAddress(),
                request.getZipcode(), request.getPhoneNumber(), request.getFromCart(), List.copyOf(items));
    }
}
