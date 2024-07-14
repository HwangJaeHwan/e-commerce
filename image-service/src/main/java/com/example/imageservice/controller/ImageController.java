package com.example.imageservice.controller;

import com.example.imageservice.domain.image.ImageType;
import com.example.imageservice.image.ImageStore;
import com.example.imageservice.request.ImageRequest;
import com.example.imageservice.response.UrlResponse;
import com.example.imageservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/image-service")
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageStore imageStore;

    @PostMapping("/upload")
    public List<UrlResponse> saveImages(@RequestPart("info") ImageRequest request, @RequestPart("images") List<MultipartFile> images) throws IOException {
        log.info("in");
        log.info("uuid = {}", request.getUuid());
        log.info("user uuid = {}", request.getUserUUID());
        log.info("type = {}", request.getImageType());
        log.info("images = {}", images);
        return imageService.saveImages(request, images);

    }

    @GetMapping("/{UUID}/urls")
    public List<UrlResponse> getUrls(@PathVariable String UUID, @RequestParam ImageType imageType) {
        return imageService.getUrls(UUID, imageType);
    }
    @GetMapping("/{filename}")
    public UrlResource showImage(@PathVariable String filename) throws
            MalformedURLException {
        log.info("show image");
        log.info("filename = {}",filename);

        return new UrlResource("file:" + imageStore.getFullPath(filename));
    }

    @DeleteMapping("/{userUUID}/{filename}")
    public void deleteImage(@PathVariable String userUUID,@PathVariable String filename) {

        imageService.deleteImage(userUUID,filename);

    }
}
