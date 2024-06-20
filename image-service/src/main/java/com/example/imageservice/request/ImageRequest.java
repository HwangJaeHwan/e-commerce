package com.example.imageservice.request;

import com.example.imageservice.domain.image.ImageType;
import lombok.Getter;

@Getter
public class ImageRequest {

    private String UUID;
    private String userUUID;
    private ImageType imageType;

}
