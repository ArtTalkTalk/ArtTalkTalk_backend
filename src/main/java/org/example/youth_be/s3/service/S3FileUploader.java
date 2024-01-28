package org.example.youth_be.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.s3.FileNameGenerator;
import org.example.youth_be.common.s3.S3Properties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3FileUploader implements FileUploader {

    private final FileNameGenerator fileNameGenerator;
    private final AmazonS3Client amazonS3Client;
    private final S3Properties s3Properties;

    @Override
    public String uploadFile(MultipartFile multipartFile, String dirName) throws Exception {
        validateFileExists(multipartFile);

        String fileName = fileNameGenerator.generateName(multipartFile.getOriginalFilename(), dirName);
        upload(multipartFile, fileName);

        return amazonS3Client.getUrl(s3Properties.getS3().getBucket(), fileName).toString();
    }

    // 업로드
    private void upload(MultipartFile multipartFile, String fileName) throws Exception {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            amazonS3Client.putObject(new PutObjectRequest(s3Properties.getS3().getBucket(), fileName, bis, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new Exception("파일 업로드에 실패했습니다.");
        }
    }

    // MultipartFile이 비어있는지 확인
    private void validateFileExists(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            throw new Exception("업로드할 파일이 없습니다.");
        }
    }
}