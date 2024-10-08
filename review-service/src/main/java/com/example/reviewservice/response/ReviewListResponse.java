package com.example.reviewservice.response;

import com.example.reviewservice.domain.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewListResponse {

    private String content;

    private double score;

    private String userUUID;

    private String reviewUUID;

    private LocalDateTime createTime;

    private String loginId;
    public ReviewListResponse(Review review) {
        this.content = review.getContent();
        this.score = review.getScore();
        this.userUUID = review.getReviewUUID();
        this.reviewUUID = review.getReviewUUID();
        this.createTime = review.getCreateTime();
    }

    public ReviewListResponse linkLoginId(String loginId) {
        this.loginId = loginId;
        return this;
    }


}
