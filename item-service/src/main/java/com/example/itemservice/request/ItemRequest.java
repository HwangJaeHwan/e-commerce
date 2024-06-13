package com.example.itemservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ItemRequest {

    @NotBlank(message = "상품 이름을 입력해주세요")
    private String name;
    @NotBlank(message = "상품 설명을 입력해주세요")
    private String itemDescription;
    @NotNull(message = "상품 가격을 입력해주세요")
    private Integer price;



}
