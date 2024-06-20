package com.example.imageservice.response;

import com.example.imageservice.domain.ImageUrl;
import lombok.Getter;

@Getter
public class UrlResponse {

    private String originalName;
    private String storedName;


    public UrlResponse(ImageUrl imageUrl) {
        this.originalName = imageUrl.getOriginalName();
        this.storedName = imageUrl.getStoredName();
    }
}
