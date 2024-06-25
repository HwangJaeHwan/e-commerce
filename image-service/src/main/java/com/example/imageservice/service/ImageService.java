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
import com.example.imageservice.response.UrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
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

        if (request.getImageType().equals(ImageType.ITEM)) {
            image = new ItemImage(request.getUserUUID(), request.getUUID());
        } else {
            image = new ReviewImage(request.getUserUUID(), request.getUUID());
        }

        return image;
    }

    public List<UrlResponse> getUrls(String uuid, ImageType imageType) {

        Image image;

        if (imageType.equals(ImageType.ITEM)) {
            image = imageRepository.getItemUrls(uuid).orElseThrow(ImageNotFoundException::new);
            return image.getUrls().stream().map(UrlResponse::new).toList();
        } else {
            image = imageRepository.getReviewUrls(uuid).orElseThrow(ImageNotFoundException::new);
        }

        return image.getUrls().stream().map(UrlResponse::new).toList();

    }
}
