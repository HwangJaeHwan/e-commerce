package com.example.imageservice.controller;

import com.example.imageservice.request.ImageRequest;
import com.example.imageservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/image-service")
@RequiredArgsConstructor
public class ItemController {

    private final ImageService imageService;

    @PostMapping("/")
    public void saveImages(@RequestPart("info") ImageRequest request, @RequestPart("images") List<MultipartFile> images) throws IOException {

        imageService.saveImages(request, images);


    }

}
