package com.example.imageservice.response;

import lombok.Getter;
import org.springframework.core.io.UrlResource;

import java.util.Base64;

@Getter
public class ImageResponse {

    private String itemUUID;
    private String imageData;
    private String mimeType;

    public ImageResponse(String itemUUID, byte[] imageData,String mimeType) {
        this.itemUUID = itemUUID;
        this.imageData = Base64.getEncoder().encodeToString(imageData);
        this.mimeType = mimeType;
    }
}
