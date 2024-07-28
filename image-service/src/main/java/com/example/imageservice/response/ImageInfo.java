package com.example.imageservice.response;

import lombok.Getter;

@Getter
public class ImageInfo {

    private String imageData;
    private String mimeType;

    public ImageInfo(String imageData, String mimeType) {
        this.imageData = imageData;
        this.mimeType = mimeType;
    }
}
