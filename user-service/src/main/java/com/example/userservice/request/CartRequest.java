package com.example.userservice.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CartRequest {

    private String itemUUID;
    private Integer quantity;


}
