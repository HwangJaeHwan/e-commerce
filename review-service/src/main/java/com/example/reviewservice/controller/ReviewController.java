package com.example.reviewservice.controller;

import com.example.reviewservice.auth.UserInfo;
import com.example.reviewservice.request.ReviewRequest;
import com.example.reviewservice.request.ReviewRevise;
import com.example.reviewservice.response.ItemScoreResponse;
import com.example.reviewservice.response.PageResponse;
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

    @PatchMapping("/revise/{reviewUUID}")
    public void reviseReview(@PathVariable String reviewUUID, ReviewRevise revise ,UserInfo userInfo) {
        reviewService.revise(reviewUUID, revise, userInfo);
    }

}
