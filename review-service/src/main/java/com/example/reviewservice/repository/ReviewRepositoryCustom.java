package com.example.reviewservice.repository;

import com.example.reviewservice.domain.Review;
import com.example.reviewservice.response.ItemScoreResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ReviewRepositoryCustom {

    Double averageScore(String itemUUID);

    List<ItemScoreResponse> averageScores(List<String> itemUUIDs);

    Page<Review> getPage(int page, String itemUUID);


}
