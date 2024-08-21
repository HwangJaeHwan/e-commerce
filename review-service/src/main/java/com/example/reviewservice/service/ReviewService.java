package com.example.reviewservice.service;

import com.example.reviewservice.auth.UserInfo;
import com.example.reviewservice.client.ImageServiceClient;
import com.example.reviewservice.client.UserServiceClient;
import com.example.reviewservice.domain.Review;
import com.example.reviewservice.exception.ReviewNotFoundException;
import com.example.reviewservice.exception.UnauthorizedException;
import com.example.reviewservice.repository.ReviewRepository;
import com.example.reviewservice.request.ReviewRequest;
import com.example.reviewservice.request.ReviewUpdate;
import com.example.reviewservice.response.ItemScoreResponse;
import com.example.reviewservice.response.PageResponse;
import com.example.reviewservice.response.ReviewListResponse;
import com.example.reviewservice.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserServiceClient userServiceClient;
    private final ImageServiceClient imageServiceClient;


    public void write(ReviewRequest request, String itemUUID, UserInfo userInfo) {

        reviewRepository.save(
                Review.builder()
                        .content(request.getContent())
                        .score(request.getScore())
                        .userUUID(userInfo.getUuid())
                        .reviewUUID(request.getReviewUUID())
                        .itemUUID(itemUUID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build()
        );

    }


    public ReviewResponse getReview(String reviewUUID) {
        Review review = reviewRepository.findByReviewUUID(reviewUUID).orElseThrow(ReviewNotFoundException::new);

        return new ReviewResponse(review);

    }

    public PageResponse getReviews(String itemUUID, int page) {

        Page<Review> reviewPage = reviewRepository.getPage(page, itemUUID);

        Set<String> uuids = reviewPage.getContent().stream().map(Review::getUserUUID).collect(Collectors.toSet());
        log.info("uuids= {}", uuids);
        Map<String, String> loginIds = userServiceClient.findLoginIds(uuids);


        log.info("ids = {}", loginIds);

        Page<ReviewListResponse> map = reviewRepository.getPage(page, itemUUID)
                .map(review -> new ReviewListResponse(review).linkLoginId(loginIds.get(review.getUserUUID())));


        return PageResponse.builder()
                .totalPage(reviewPage.getTotalPages())
                .totalElement(reviewPage.getTotalElements())
                .isFirst(reviewPage.isFirst())
                .isLast(reviewPage.isLast())
                .items(map.getContent())
                .build();

    }

    public void update(String reviewUUID, ReviewUpdate update , UserInfo userInfo) {
        log.info("content = {}",update.getContent());
        log.info("score = {}",update.getScore());

        Review review = reviewRepository.findByReviewUUID(reviewUUID).orElseThrow(ReviewNotFoundException::new);

        if (!review.getUserUUID().equals(userInfo.getUuid())) {
            throw new UnauthorizedException();
        }

        review.updateContent(update.getContent());
        review.updateScore(update.getScore());
        review.changeUpdateTime(LocalDateTime.now());

    }

    public Double getAverageScore(String itemUUID) {
        return reviewRepository.averageScore(itemUUID);
    }

    public List<ItemScoreResponse> getAverageScores(List<String> itemUUIDs) {

        return reviewRepository.averageScores(itemUUIDs);


    }

    public void delete(String reviewUUID) {

        imageServiceClient.deleteReviewImage(reviewUUID);
        reviewRepository.deleteReviewByReviewUUID(reviewUUID);

    }
}
