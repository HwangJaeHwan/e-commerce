package com.example.imageservice.image;

import com.example.imageservice.domain.ImageUrl;
import com.example.imageservice.domain.image.Image;
import com.example.imageservice.repository.ImageRepository;
import com.example.imageservice.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageStore {

    @Value("${file.dir}")
    private String fileDir;

    private final ImageRepository imageRepository;
    private final UrlRepository urlRepository;
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public void storeImages(Image image, List<MultipartFile> multipartFiles)
            throws IOException {

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                image.addUrl(storeImage(multipartFile));
            }
        }

    }


    private ImageUrl storeImage(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return null;
        }


        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new ImageUrl(originalFilename, storeFileName);
}

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
