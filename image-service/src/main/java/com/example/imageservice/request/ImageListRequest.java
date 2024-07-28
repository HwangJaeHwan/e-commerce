package com.example.imageservice.request;

import com.example.imageservice.domain.image.ImageType;
import lombok.Getter;

import java.util.List;

@Getter
public class ImageListRequest {


    private ImageType imageType;

    private List<String> uuids;
}
