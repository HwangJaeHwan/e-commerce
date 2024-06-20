package com.example.itemservice.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private String content;

    private double score;

    private String userUUID;

    private String reviewUUID;

    private LocalDateTime createTime;

}