package com.example.reviewservice.request;

import lombok.Getter;

@Getter
public class ReviewRevise {

    private String userUUID;
    private String content;
    private double score;

}
