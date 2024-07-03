package com.example.payservice.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ItemQuantity {

    @NotNull
    private String itemUUID;
    @Min(value = 1)
    private int quantity;


}
