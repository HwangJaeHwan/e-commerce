package com.example.reviewservice.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequest {

    @NotNull(message = "유저 ID 값이 없습니다.")
    private String userUUID;

    @NotBlank(message = "리뷰 제목을 입력해주세요")
    private String title;

    @NotBlank(message = "리뷰 내용을 입력해주세요")
    private String content;

    @Min(value = 0, message = "점수를 매겨주세요")
    private double score;

}
