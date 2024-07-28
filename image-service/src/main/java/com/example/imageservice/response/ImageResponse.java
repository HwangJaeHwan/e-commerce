package com.example.imageservice.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Getter
public class ImageResponse {

    private String uuid;
    private List<ImageInfo> imageInfos = new ArrayList<>();

    public ImageResponse(String uuid) {
        this.uuid = uuid;
    }
}
