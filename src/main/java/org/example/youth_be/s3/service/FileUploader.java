package org.example.youth_be.s3.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    String uploadFile(MultipartFile multipartFile, String dirName) throws Exception;
}
