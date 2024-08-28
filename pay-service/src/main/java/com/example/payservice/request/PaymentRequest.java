package com.example.payservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PaymentRequest {

    @NotNull
    private String impUid;
    @NotNull
    private List<ItemQuantity> items;

}
