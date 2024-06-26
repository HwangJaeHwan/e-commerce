package com.example.reviewservice.controller;

import com.example.reviewservice.request.ReviewRequest;
import com.example.reviewservice.request.ReviewRevise;
import com.example.reviewservice.response.PageResponse;
import com.example.reviewservice.response.ReviewResponse;
import com.example.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/review-service")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/write")
    public void writeReview(@RequestBody @Valid ReviewRequest request) {
        reviewService.write(request);
    }

    @GetMapping("/{itemUUID}")
    public PageResponse getReviews(@PathVariable String itemUUID, @RequestParam(defaultValue = "1") int page) {

        return reviewService.getReviews(itemUUID, page);
    }

    @GetMapping("/{itemUUID}/score")
    public Double getAverageScore(@PathVariable String itemUUID) {
        return reviewService.getScoreAverage(itemUUID);
    }

    @PatchMapping("/revise/{reviewId}")
    public void reviseReview(@PathVariable Long reviewId, ReviewRevise revise) {
        reviewService.revise(reviewId, revise);
    }

}
