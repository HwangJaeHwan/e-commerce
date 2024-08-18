package com.example.itemservice.request;

import com.example.itemservice.domain.item.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ItemUpdate {

    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "상품 설명을 입력해주세요")
    private String itemDescription;
    @NotNull(message = "상품 가격을 입력해주세요")
    private Integer price;
    @NotNull
    private String itemUUID;
    @Min(value = 1, message = "상품의 수량을 입력해주세요")
    private int stock;
    @NotNull(message = "카테고리 설정을 해주세요")
    private Category category;

}
