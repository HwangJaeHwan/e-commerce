package com.example.payservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentRequest {

    @NotNull
    private String impUid;
    @NotNull
    private List<ItemQuantity> items;

}
