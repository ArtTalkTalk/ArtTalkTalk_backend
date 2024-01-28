package org.example.youth_be.s3.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    String uploadProfileImage(MultipartFile file) throws Exception;
}
