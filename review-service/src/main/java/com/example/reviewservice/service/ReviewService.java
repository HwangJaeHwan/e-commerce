package com.example.reviewservice.service;

import com.example.reviewservice.client.UserServiceClient;
import com.example.reviewservice.domain.Review;
import com.example.reviewservice.exception.ReviewNotFoundException;
import com.example.reviewservice.exception.UnauthorizedException;
import com.example.reviewservice.repository.ReviewRepository;
import com.example.reviewservice.request.ReviewRequest;
import com.example.reviewservice.request.ReviewRevise;
import com.example.reviewservice.response.ItemScoreResponse;
import com.example.reviewservice.response.PageResponse;
import com.example.reviewservice.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserServiceClient userServiceClient;


    public void write(ReviewRequest request, String itemUUID) {

        reviewRepository.save(
                Review.builder()
                        .content(request.getContent())
                        .score(request.getScore())
                        .userUUID(request.getUserUUID())
                        .reviewUUID(UUID.randomUUID().toString())
                        .itemUUID(itemUUID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build()
        );

    }


    public PageResponse getReviews(String itemUUID, int page) {

        Page<Review> reviewPage = reviewRepository.getPage(page, itemUUID);

        Set<String> uuids = reviewPage.getContent().stream().map(Review::getUserUUID).collect(Collectors.toSet());
        Map<String, String> loginIds = userServiceClient.findLoginIds(uuids);

        log.info("uuids= {}", uuids);
        log.info("ids = {}", loginIds);

        Page<ReviewResponse> map = reviewRepository.getPage(page, itemUUID)
                .map(review -> new ReviewResponse(review).linkLoginId(loginIds.get(review.getUserUUID())));


        return PageResponse.builder()
                .totalPage(reviewPage.getTotalPages())
                .totalElement(reviewPage.getTotalElements())
                .isFirst(reviewPage.isFirst())
                .isLast(reviewPage.isLast())
                .items(map.getContent())
                .build();

    }

    public void revise(Long reviewId, ReviewRevise revise) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

        if (!review.getUserUUID().equals(revise.getUserUUID())) {
            throw new UnauthorizedException();
        }

        review.reviseContent(revise.getContent());
        review.reviseScore(revise.getScore());
        review.changeUpdateTime(LocalDateTime.now());

    }

    public Double getAverageScore(String itemUUID) {
        return reviewRepository.averageScore(itemUUID);
    }

    public List<ItemScoreResponse> getAverageScores(List<String> itemUUIDs) {

        return reviewRepository.averageScores(itemUUIDs);


    }
}
