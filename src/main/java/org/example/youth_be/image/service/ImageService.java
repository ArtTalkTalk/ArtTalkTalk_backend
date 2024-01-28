package org.example.youth_be.image.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.image.service.request.ImageUploadRequest;
import org.example.youth_be.image.service.response.UploadImageResponse;
import org.example.youth_be.s3.service.FileUploader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final FileUploader fileUploader;

    public UploadImageResponse uploadImage(ImageUploadRequest request) {
        try {
            String imageUrl = fileUploader.uploadProfileImage(request.getFile());
            return UploadImageResponse.of(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
