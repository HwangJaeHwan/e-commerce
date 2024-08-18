package com.example.imageservice.response;

import lombok.Getter;

@Getter
public class ImageInfo {

    private Long id;
    private String imageData;
    private String mimeType;

    public ImageInfo(Long id, String imageData, String mimeType) {
        this.id = id;
        this.imageData = imageData;
        this.mimeType = mimeType;
    }

}
