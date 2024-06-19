package com.example.reviewservice.service;

import com.example.reviewservice.domain.Review;
import com.example.reviewservice.repository.ReviewRepository;
import com.example.reviewservice.request.ReviewRequest;
import com.example.reviewservice.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                        .build()
        );

    }


    public List<ReviewResponse> getReviews(String itemUUID) {
        return reviewRepository.findAllByItemUUID(itemUUID).stream().map(ReviewResponse::new).toList();
    }
}
