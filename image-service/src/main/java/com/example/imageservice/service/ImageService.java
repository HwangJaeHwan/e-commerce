package com.example.imageservice.service;

import com.example.imageservice.domain.ImageUrl;
import com.example.imageservice.domain.image.Image;
import com.example.imageservice.domain.image.ImageType;
import com.example.imageservice.domain.image.ItemImage;
import com.example.imageservice.domain.image.ReviewImage;
import com.example.imageservice.exception.ImageNotFoundException;
import com.example.imageservice.exception.UnauthorizedException;
import com.example.imageservice.image.ImageStore;
import com.example.imageservice.repository.ImageRepository;
import com.example.imageservice.repository.UrlRepository;
import com.example.imageservice.request.ImageRequest;
import com.example.imageservice.request.ItemRequest;
import com.example.imageservice.response.ImageResponse;
import com.example.imageservice.response.UrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UrlRepository urlRepository;
    private final ImageStore imageStore;


    public List<UrlResponse> saveImages(ImageRequest request, List<MultipartFile> images) throws IOException {

        Image image = typeCheck(request);

        imageStore.storeImages(image,images);

        imageRepository.save(image);


        return image.getUrls().stream().map(UrlResponse::new).toList();


    }

    public void deleteImage(String userUUID, String filename) {

        ImageUrl url = urlRepository.findBystoredName(filename).orElseThrow(ImageNotFoundException::new);

        if (!url.getImage().getUserUUID().equals(userUUID)) {
            throw new UnauthorizedException();
        }

        imageStore.deleteImage(filename);

        urlRepository.delete(url);


    }

    private Image typeCheck(ImageRequest request) {
        Image image;

        log.info("request.getUUID() = {}",request.getUuid());
        log.info("request.getUserUUID() = {}",request.getUserUUID());
        if (request.getImageType().equals(ImageType.ITEM)) {
            log.info("아이템 이미지!");
            image = new ItemImage(request.getUserUUID(), request.getUuid());
        } else {
            image = new ReviewImage(request.getUserUUID(), request.getUuid());
        }

        return image;
    }

    public List<UrlResponse> getUrls(String uuid, ImageType imageType) {

        Image image;

        if (imageType.equals(ImageType.ITEM)) {
            image = imageRepository.getItemImage(uuid).orElseThrow(ImageNotFoundException::new);
            return image.getUrls().stream().map(UrlResponse::new).toList();
        } else {
            image = imageRepository.getReviewImage(uuid).orElseThrow(ImageNotFoundException::new);
        }

        return image.getUrls().stream().map(UrlResponse::new).toList();

    }

    public List<ImageResponse> getImages(List<String> uuids) throws IOException {

        List<ImageResponse> responses = new ArrayList<>();

        for (String itemUUiD : uuids) {
            ItemImage image = imageRepository.getItemImage(itemUUiD).orElseThrow(ImageNotFoundException::new);
            ImageUrl url = image.getUrls().getFirst();

            UrlResource urlResource = new UrlResource("file:/" + imageStore.getFullPath(url.getStoredName()));
            log.info("URL = {}", urlResource.getURI());
            String mimeType = Files.probeContentType(Paths.get(urlResource.getURI()));
            byte[] imageData = StreamUtils.copyToByteArray(urlResource.getInputStream());
            log.info("mimeType = {}", mimeType);

            responses.add(new ImageResponse(itemUUiD, imageData,mimeType));

        }

        return responses;
    }
}
