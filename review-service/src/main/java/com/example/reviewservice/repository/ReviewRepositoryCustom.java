package com.example.reviewservice.repository;

import com.example.reviewservice.domain.Review;
import org.springframework.data.domain.Page;


public interface ReviewRepositoryCustom {

    Double averageScore(String itemUUID);

    Page<Review> getPage(int page, String itemUUID);


}
