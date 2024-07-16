package com.example.imageservice.response;

import lombok.Getter;
import org.springframework.core.io.UrlResource;

@Getter
public class ImageResponse {

    private String itemUUIds;
    private byte[] imageData;
    private String mimeType;

    public ImageResponse(String itemUUIds, byte[] imageData,String mimeType) {
        this.itemUUIds = itemUUIds;
        this.imageData = imageData;
        this.mimeType = mimeType;
    }
}
