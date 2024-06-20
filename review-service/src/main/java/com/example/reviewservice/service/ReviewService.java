package com.example.reviewservice.service;

import com.example.reviewservice.domain.Review;
import com.example.reviewservice.repository.ReviewRepository;
import com.example.reviewservice.request.ReviewRequest;
import com.example.reviewservice.response.PageResponse;
import com.example.reviewservice.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;


    public void write(ReviewRequest request) {

        reviewRepository.save(
                Review.builder()
                        .content(request.getContent())
                        .score(request.getScore())
                        .userUUID(request.getUserUUID())
                        .reviewUUID(UUID.randomUUID().toString())
                        .itemUUID(request.getItemUUID())
                        .build()
        );

    }


    public PageResponse getReviews(String itemUUID, int page) {

        Page<Review> reviewPage = reviewRepository.getPage(page, itemUUID);

        return PageResponse.builder()
                .totalPage(reviewPage.getTotalPages())
                .totalElement(reviewPage.getTotalElements())
                .isFirst(reviewPage.isFirst())
                .isLast(reviewPage.isLast())
                .reviews(reviewPage.map(ReviewResponse::new).toList())
                .build();

    }
}
