package com.example.reviewservice.response;

import com.example.reviewservice.domain.Review;
import lombok.Getter;

@Getter
public class ReviewResponse {

    private String content;

    private Double score;

    private String reviewUUID;

    public ReviewResponse(Review review) {
        this.content = review.getContent();
        this.score = review.getScore();
        this.reviewUUID = review.getReviewUUID();
    }
}
