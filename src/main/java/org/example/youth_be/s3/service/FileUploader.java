package org.example.youth_be.s3.service;

import org.example.youth_be.image.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    String upload(MultipartFile file, ImageType imageType);

    void delete(String fileName);
}
