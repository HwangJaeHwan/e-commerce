package com.example.imageservice.service;

import com.example.imageservice.auth.UserInfo;
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
import com.example.imageservice.request.ImageListRequest;
import com.example.imageservice.request.ImageRequest;
import com.example.imageservice.response.ImageInfo;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UrlRepository urlRepository;
    private final ImageStore imageStore;


    public List<UrlResponse> saveImages(ImageRequest request, List<MultipartFile> images
            ,UserInfo userInfo) throws IOException {

        Image image = typeCheck(request, userInfo);

        imageStore.storeImages(image,images);

        imageRepository.save(image);


        return image.getUrls().stream().map(UrlResponse::new).toList();


    }

    public void deleteImage(String userUUID, String filename) {

        ImageUrl url = urlRepository.findByStoredName(filename).orElseThrow(ImageNotFoundException::new);

        if (!url.getImage().getUserUUID().equals(userUUID)) {
            throw new UnauthorizedException();
        }

        imageStore.deleteImage(filename);

        urlRepository.delete(url);


    }

    private Image typeCheck(ImageRequest request, UserInfo userInfo) {
        Image image;

        log.info("request.getUUID() = {}",request.getUuid());
        log.info("userUUID = {}", userInfo.getUuid());

        if (request.getImageType().equals(ImageType.ITEM)) {
            log.info("아이템 이미지!");
            image = new ItemImage(userInfo.getUuid(), request.getUuid());
        } else {
            image = new ReviewImage(userInfo.getUuid(), request.getUuid());
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

    public List<ImageResponse> getImages(ImageListRequest imageListRequest) throws IOException {


        if (imageListRequest.getImageType().equals(ImageType.ITEM)) {
            return getItemImageList(imageListRequest);
        }


        return getReviewImageList(imageListRequest);

    }

    private List<ImageResponse> getItemImageList(ImageListRequest imageListRequest) throws IOException {
        List<ImageResponse> responses = new ArrayList<>();
        List<ItemImage> images = imageRepository.getItemsImage(imageListRequest.getUuids());

        for (ItemImage itemImage : images) {

            responses.add(getImageResponse(itemImage.getItemUUID(), itemImage.getUrls()));

        }

        return responses;
    }

    private List<ImageResponse> getReviewImageList(ImageListRequest imageListRequest) throws IOException {
        List<ImageResponse> responses = new ArrayList<>();
        List<ReviewImage> images = imageRepository.getReviewsImage(imageListRequest.getUuids());

        for (ReviewImage reviewImage : images) {

            responses.add(getImageResponse(reviewImage.getReviewUUID(), reviewImage.getUrls()));

        }

        return responses;
    }

    private String ByteToString(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }

    private ImageResponse getImageResponse(String uuid, List<ImageUrl> urls) throws IOException {
        ImageResponse response = new ImageResponse(uuid);

        for (ImageUrl url : urls) {

            UrlResource urlResource = new UrlResource("file:/" + imageStore.getFullPath(url.getStoredName()));
            log.info("URL = {}", urlResource.getURI());
            String mimeType = Files.probeContentType(Paths.get(urlResource.getURI()));
            byte[] imageData = StreamUtils.copyToByteArray(urlResource.getInputStream());
            log.info("mimeType = {}", mimeType);

            response.getImageInfos().add(new ImageInfo(ByteToString(imageData), mimeType));


        }

        return response;
    }

    public ImageResponse getItemImages(String itemUUID) throws IOException {
        List<ImageResponse> responses = new ArrayList<>();

        ItemImage image = imageRepository.getItemImage(itemUUID).orElseThrow(ImageNotFoundException::new);
        List<ImageUrl> urls = image.getUrls();

        log.info("size = {}", urls.size());

        return getImageResponse(image.getItemUUID(), urls);




    }

}
