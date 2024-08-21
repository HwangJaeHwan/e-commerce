package com.example.reviewservice.controller;

import com.example.reviewservice.auth.UserInfo;
import com.example.reviewservice.request.ReviewRequest;
import com.example.reviewservice.request.ReviewUpdate;
import com.example.reviewservice.response.ItemScoreResponse;
import com.example.reviewservice.response.PageResponse;
import com.example.reviewservice.response.ReviewResponse;
import com.example.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/review-service")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/write/{itemUUID}")
    public void writeReview(@PathVariable String itemUUID, @RequestBody @Valid ReviewRequest request
            , UserInfo userInfo) {
        reviewService.write(request,itemUUID,userInfo);
    }

    @GetMapping("/get/{reviewUUID}")
    public ReviewResponse getReview(@PathVariable String reviewUUID) {
        return reviewService.getReview(reviewUUID);
    }

    @GetMapping("/{itemUUID}")
    public PageResponse getReviews(@PathVariable String itemUUID, @RequestParam(defaultValue = "1") int page) {

        return reviewService.getReviews(itemUUID, page);
    }

    @GetMapping("/{itemUUID}/score")
    public Double getAverageScore(@PathVariable String itemUUID) {
        return reviewService.getAverageScore(itemUUID);
    }

    @PostMapping("/scores")
    public Map<String,Double> getAverageScores(@RequestBody List<String> itemUUIDs) {

        return reviewService.getAverageScores(itemUUIDs)
                .stream()
                .collect(Collectors.toMap(
                        ItemScoreResponse::getItemUUID,
                        ItemScoreResponse::getScore
                ));

    }

    @PatchMapping("/update/{reviewUUID}")
    public void updateReview(@PathVariable String reviewUUID, @RequestBody ReviewUpdate update , UserInfo userInfo) {
        reviewService.update(reviewUUID, update, userInfo);
    }

    @DeleteMapping("/{reviewUUID}")
    public void deleteReview(@PathVariable String reviewUUID) {

        reviewService.delete(reviewUUID);
    }

}
