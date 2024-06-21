package com.example.imageservice.repository;

import com.example.imageservice.domain.image.Image;
import com.example.imageservice.domain.image.ImageType;
import com.example.imageservice.domain.image.ItemImage;
import com.example.imageservice.domain.image.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select i from ItemImage i where i.itemUUID = :UUID")
    Optional<ItemImage> getItemUrls(String UUID);

    @Query("select i from ReviewImage i where i.reviewUUID = :UUID")
    Optional<ReviewImage> getReviewUrls(String UUID);
}
