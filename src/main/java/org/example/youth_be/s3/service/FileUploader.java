package org.example.youth_be.s3.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    String upload(MultipartFile file);

    void delete(String fileName);
}
