package com.example.imageservice.controller;

import com.example.imageservice.auth.UserInfo;
import com.example.imageservice.domain.image.ImageType;
import com.example.imageservice.image.ImageStore;
import com.example.imageservice.request.ImageListRequest;
import com.example.imageservice.request.ImageRequest;
import com.example.imageservice.request.ImageUpdateRequest;
import com.example.imageservice.response.ImageResponse;
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
    public List<UrlResponse> saveImages(@RequestPart("info") ImageRequest request,
                                        @RequestPart("images") List<MultipartFile> images,
                                        UserInfo userInfo) throws IOException {
        log.info("in");
        log.info("uuid = {}", request.getUuid());
        log.info("user uuid = {}", userInfo.getUuid());
        log.info("type = {}", request.getImageType());
        log.info("images = {}", images);
        return imageService.saveImages(request, images, userInfo);

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

        return new UrlResource("file:/" + imageStore.getFullPath(filename));
    }

    @PostMapping("/images")
    public List<ImageResponse> images(@RequestBody ImageListRequest imageListRequest) throws IOException {

        return imageService.getImages(imageListRequest);

    }

    @GetMapping("/images/item/{itemUUID}")
    public ImageResponse itemImages(@PathVariable String itemUUID) throws IOException {
        log.info("/images/{}" ,itemUUID);

        return imageService.getImages(itemUUID,ImageType.ITEM);
    }

    @GetMapping("/images/review/{reviewUUID}")
    public ImageResponse reviewImages(@PathVariable String reviewUUID) throws IOException {

        return imageService.getImages(reviewUUID,ImageType.REVIEW);
    }

    @PostMapping("/update/{UUID}")
    public void updateImage(@PathVariable String UUID, @RequestPart("info") ImageUpdateRequest imageUpdateRequest
            ,@RequestPart(value = "images", required = false) List<MultipartFile> images,UserInfo userInfo) throws IOException {
        log.info("update uuid = {}", UUID);

        imageService.updateImage(userInfo, UUID, imageUpdateRequest , images);
    }

    @DeleteMapping("/{userUUID}/{filename}")
    public void deleteImage(@PathVariable String userUUID,@PathVariable String filename) {

        imageService.deleteUrlById(userUUID,filename);

    }

    @DeleteMapping("/review/{reviewUUID}")
    public void deleteReviewImage(@PathVariable String reviewUUID){
        imageService.deleteImage(reviewUUID, ImageType.REVIEW);
    }

    @DeleteMapping("/image/{itemUUID}")
    public void deleteImageImage(@PathVariable String itemUUID){
        imageService.deleteImage(itemUUID, ImageType.ITEM);
    }
}
