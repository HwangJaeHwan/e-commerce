package com.example.imageservice.domain.image;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorValue("REVIEW")
public class ReviewImage extends Image{

    private String reviewUUID;

    public ReviewImage(String reviewUUID) {
        this.reviewUUID = reviewUUID;
    }
}
