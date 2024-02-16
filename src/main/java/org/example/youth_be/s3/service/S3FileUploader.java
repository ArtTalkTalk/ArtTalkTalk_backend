package org.example.youth_be.s3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.exceptions.YouthInternalException;
import org.example.youth_be.common.s3.FileNameGenerator;
import org.example.youth_be.common.s3.S3Properties;
import org.example.youth_be.image.enums.ImageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3FileUploader implements FileUploader {

    private final FileNameGenerator fileNameGenerator;
    private final AmazonS3Client amazonS3Client;
    private final S3Properties s3Properties;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String upload(MultipartFile file, ImageType imageType) {
        try {
            validateFileExists(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String dirName;

        if (imageType.equals(ImageType.PROFILE)) {
            dirName = s3Properties.getUploadDirs().getProfileDirName();
        } else {
            dirName = s3Properties.getUploadDirs().getArtworkDirName();
        }

        String fileName = fileNameGenerator.generateName(file.getOriginalFilename(), dirName);

        try {
            log.info("[S3 File Upload] 시작 :{}", fileName);
            logger.info("[S3 File Upload] 시작 :{}", fileName);
            amazonS3Client.putObject(s3Properties.getS3().getBucket(), fileName, file.getInputStream(), getObjectMetadata(file));
            log.info("[S3 File Upload] 완료 :{}", fileName);
            logger.info("[S3 File Upload] 시작 :{}", fileName);

            URL fileUrl = amazonS3Client.getUrl(s3Properties.getS3().getBucket(), fileName);
            return fileUrl.toString();

        } catch (SdkClientException | IOException e) {
            log.error("[S3 File Upload 실패]", e);
            throw new YouthInternalException("[S3 File Upload 실패]", e.getMessage());
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            amazonS3Client.deleteObject(s3Properties.getS3().getBucket(), fileName);
            log.info("[S3 File Delete] {}", fileName);
        } catch (SdkClientException e) {
            log.error("[S3 File Delete 실패]", e);
            throw new YouthInternalException("[S3 File Delete 실패]", e.getMessage());
        }
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    // MultipartFile이 비어있는지 확인
    private void validateFileExists(MultipartFile multipartFile){
        if (multipartFile.isEmpty()) {
            throw new YouthBadRequestException("업로드할 파일이 없습니다.", null);
        }
    }
}