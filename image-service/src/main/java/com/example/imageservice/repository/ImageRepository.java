package com.example.imageservice.repository;

import com.example.imageservice.domain.image.Image;
import com.example.imageservice.domain.image.ItemImage;
import com.example.imageservice.domain.image.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select i from ItemImage i join fetch i.urls where i.itemUUID = :UUID")
    Optional<ItemImage> getItemImage(@Param("UUID") String UUID);

    @Query("select i from ReviewImage i join fetch i.urls where i.reviewUUID = :UUID")
    Optional<ReviewImage> getReviewImage(String UUID);


    @Query("select i from ItemImage i join fetch i.urls where i.itemUUID in :UUIDs")
    List<ItemImage> getItemsImage(@Param("UUIDs") List<String> UUIDs);
    @Query("select i from ReviewImage i join fetch i.urls where i.reviewUUID in :UUIDs")
    List<ReviewImage> getReviewsImage(@Param("UUIDs")List<String> UUIDs);
}
