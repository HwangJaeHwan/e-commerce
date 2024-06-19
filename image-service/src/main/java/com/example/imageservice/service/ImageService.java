package com.example.imageservice.service;

import com.example.imageservice.domain.image.Image;
import com.example.imageservice.domain.image.ImageType;
import com.example.imageservice.domain.image.ItemImage;
import com.example.imageservice.domain.image.ReviewImage;
import com.example.imageservice.image.ImageStore;
import com.example.imageservice.repository.ImageRepository;
import com.example.imageservice.repository.UrlRepository;
import com.example.imageservice.request.ImageRequest;
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


    public void saveImages(ImageRequest request, List<MultipartFile> images) throws IOException {

        Image image = typeCheck(request);

        imageStore.storeImages(image,images);


        imageRepository.save(image);


    }

    private Image typeCheck(ImageRequest request) {
        Image image;

        if (request.getImageType().equals(ImageType.ITEM)) {
            image = new ItemImage(request.getUUID());
        } else {
            image = new ReviewImage(request.getUUID());
        }

        return image;
    }
}
