package com.example.reviewservice.repository;

import com.example.reviewservice.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    Optional<Review> findByReviewUUID(String reviewUUID);
    List<Review> findAllByItemUUID(String itemUUID);

}
