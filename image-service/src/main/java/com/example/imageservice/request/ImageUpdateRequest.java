package com.example.imageservice.request;

import com.example.imageservice.domain.image.ImageType;
import lombok.Getter;

import java.util.List;

@Getter
public class ImageUpdateRequest {

    List<Long> ids;
    ImageType imageType;
}
