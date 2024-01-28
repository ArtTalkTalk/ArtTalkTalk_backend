package org.example.youth_be.image.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.image.service.dto.UploadImageResponse;
import org.example.youth_be.s3.service.FileUploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final FileUploader fileUploader;

    public UploadImageResponse uploadImage(MultipartFile multipartFile) {
        try {
            String imageUrl = fileUploader.uploadFile(multipartFile, "images");
            return UploadImageResponse.of(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
