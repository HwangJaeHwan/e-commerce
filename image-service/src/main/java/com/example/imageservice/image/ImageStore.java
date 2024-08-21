package com.example.imageservice.image;

import com.example.imageservice.domain.ImageUrl;
import com.example.imageservice.domain.image.Image;
import com.example.imageservice.exception.ImageDeleteFailedException;
import com.example.imageservice.exception.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public void storeImages(Image image, List<MultipartFile> multipartFiles)
            throws IOException {

        if (multipartFiles == null) {
            return;
        }

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                image.addUrl(storeImage(multipartFile));
            }
        }

    }

    public void deleteImage(String filename) {
        File file = new File(getFullPath(filename));

        if (file.exists()) {
            if (!file.delete()) {
                throw new ImageDeleteFailedException();
            }
        } else {
            throw new ImageNotFoundException();
        }
    }


    private ImageUrl storeImage(MultipartFile multipartFile) throws IOException {

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
