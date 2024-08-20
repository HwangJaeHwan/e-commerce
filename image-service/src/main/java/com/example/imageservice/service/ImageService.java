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
import com.example.imageservice.request.ImageUpdateRequest;
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
import java.io.InputStream;
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

    public void deleteUrl(String userUUID, String filename) {

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

    public ImageResponse getImages(String UUID, ImageType imageType) throws IOException {
        List<ImageResponse> responses = new ArrayList<>();

        if (imageType.equals(ImageType.ITEM)) {
            ItemImage image = imageRepository.getItemImage(UUID).orElseThrow(ImageNotFoundException::new);

            return getImageResponse(image.getItemUUID(), image.getUrls());
        }

        ReviewImage image = imageRepository.getReviewImage(UUID).orElseThrow(ImageNotFoundException::new);

        return getImageResponse(image.getReviewUUID(), image.getUrls());





    }

    public void updateImage(UserInfo userInfo, String uuid, ImageUpdateRequest request, List<MultipartFile>images) throws IOException {

        Image image;


        if (request.getImageType() == ImageType.ITEM) {
            image = imageRepository.getItemImage(uuid).orElseThrow(ImageNotFoundException::new);
        } else {
            image = imageRepository.getReviewImage(uuid).orElseThrow(ImageNotFoundException::new);
        }

        log.info("find image user uuid = {}", image.getUserUUID());
        log.info("userInfo uuid = {}", userInfo.getUuid());
        log.info("ids.size() = {}", request.getIds().size());

        if (!userInfo.getUuid().equals(image.getUserUUID())) {
            throw new UnauthorizedException();
        }

        if (!request.getIds().isEmpty()) {

            for (Long id : request.getIds()) {
                deleteUrl(id);
            }

        }

        imageStore.storeImages(image, images);


    }

    private void deleteUrl(Long id) {

        log.info("delete id = {}", id);
        ImageUrl url = urlRepository.findById(id).orElseThrow(ImageNotFoundException::new);
        imageStore.deleteImage(url.getStoredName());
        log.info("delete 전");
        urlRepository.delete(url);
        log.info("delete 후");

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

            try (InputStream inputStream = urlResource.getInputStream()) {
                byte[] imageData = StreamUtils.copyToByteArray(inputStream);
                log.info("mimeType = {}", mimeType);

                response.getImageInfos().add(new ImageInfo(url.getId(),ByteToString(imageData), mimeType));
            } catch (IOException e) {
                log.info("시발 어떻게 하라고");
            }

        }

        return response;
    }

}
