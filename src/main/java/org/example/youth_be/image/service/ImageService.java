package org.example.youth_be.image.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.image.repository.ImageRepository;
import org.example.youth_be.image.service.dto.UploadImageResponse;
import org.example.youth_be.s3.service.S3UploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3UploadService s3UploadService;

    public UploadImageResponse uploadImage(MultipartFile multipartFile) {
        try {
            String imageUrl = s3UploadService.uploadFile(multipartFile, "images");
            return UploadImageResponse.builder()
                    .imageUrl(imageUrl)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
