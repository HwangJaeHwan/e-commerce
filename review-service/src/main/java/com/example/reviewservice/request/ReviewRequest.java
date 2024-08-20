package com.example.reviewservice.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequest {


    @NotBlank(message = "리뷰 내용을 입력해주세요")
    private String content;

    @NotBlank(message = "리뷰 UUID가 존재하지 않습니다.")
    private String reviewUUID;

    @Min(value = 0, message = "점수를 매겨주세요")
    private double score;


}
