package com.example.imageservice.request;

import com.example.imageservice.domain.image.ImageType;
import lombok.Getter;

@Getter
public class ImageRequest {

    private String uuid;
    private ImageType imageType;

}
