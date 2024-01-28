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

    public UploadImageResponse uploadImage(ImageUploadRequest request) throws Exception {
            String imageUrl = fileUploader.uploadProfileImage(request.getFile());
            return UploadImageResponse.of(imageUrl);
    }
}
