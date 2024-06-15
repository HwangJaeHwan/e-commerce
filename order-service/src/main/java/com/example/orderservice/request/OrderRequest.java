package com.example.orderservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {

    @NotNull(message = "userID는 필수입니다.")
    private String userUUID;
    @NotNull(message = "상품이 없습니다.")
    private List<ItemRequest> items;
    @NotNull(message = "주소가 없습니다.")
    private String city;
    @NotNull(message = "주소가 없습니다.")
    private String street;
    @NotNull(message = "주소가 없습니다.")
    private String zipcode;

}
