package com.example.orderservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class OrderRequest {

    @NotNull(message = "userID는 필수입니다.")
    private String userUUID;
    @NotNull(message = "상품이 없습니다.")
    private List<ItemRequest> items;
    @NotBlank(message = "주소가 없습니다.")
    private String address;

    @NotBlank(message = "배송지 명이 없습니다.")
    private String name;

    @NotNull(message = "결제id 값이 없습니다.")
    private String impUid;

    private String detailAddress;
    @NotBlank(message = "우편번호가 없습니다.")
    private String zipcode;
    @NotBlank(message = "전화번호가 없습니다.")
    private String phoneNumber;

    @NotNull
    private Boolean fromCart;


}
