package org.example.youth_be.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.s3.FileNameBuildUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3UploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public String uploadFile(MultipartFile multipartFile, String dirName) throws Exception {
        validateFileExists(multipartFile);

        String fileName = FileNameBuildUtils.buildFileName(multipartFile.getOriginalFilename(), dirName);
        upload(multipartFile, fileName);

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 업로드
    private void upload(MultipartFile multipartFile, String fileName) throws Exception {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
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