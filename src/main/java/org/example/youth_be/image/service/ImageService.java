package org.example.youth_be.image.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.image.domain.ImageEntity;
import org.example.youth_be.image.enums.ImageType;
import org.example.youth_be.image.repository.ImageRepository;
import org.example.youth_be.image.service.request.DeleteImageRequest;
import org.example.youth_be.image.service.request.ImageUploadRequest;
import org.example.youth_be.image.service.response.UploadArtworkImageResponse;
import org.example.youth_be.image.service.response.UploadArtworkResponse;
import org.example.youth_be.image.service.response.UploadImageResponse;
import org.example.youth_be.s3.service.FileUploader;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final FileUploader fileUploader;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public UploadImageResponse uploadImage(ImageUploadRequest request) {
            String imageUrl = fileUploader.upload(request.getFile(), ImageType.PROFILE);
            return UploadImageResponse.of(imageUrl);
    }

    @Transactional
    public UploadArtworkImageResponse uploadArtworkImage(ImageUploadRequest request) {
        String imageUrl = fileUploader.upload(request.getFile(), ImageType.PROFILE);
        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        ImageEntity imageEntity = ImageEntity.builder()
                .imageUrl(imageUrl)
                .imageUploadName(fileName)
                .build();
        imageRepository.save(imageEntity);
        return UploadArtworkImageResponse.of(imageUrl, imageEntity.getImageId());
    }

    public UploadArtworkResponse uploadImages(MultipartFile[] images) {
        // MultipartFile 배열을 스트림으로 변환하고, 각 파일에 대해 처리
        List<String> fileUrls = Arrays.stream(images)
                .filter(multipartFile -> !multipartFile.isEmpty()) // 비어있지 않은 파일만 처리
                .map(multipartFile -> {
                    return fileUploader.upload(multipartFile, ImageType.ARTWORK);
                })
                .collect(Collectors.toList()); // 결과 URL을 리스트로 수집

        // fileUrls에서 파일 이름만 추출
        List<String> fileNames = fileUrls.stream()
                .map(uploadUrl -> uploadUrl.substring(uploadUrl.lastIndexOf('/') + 1))
                .collect(Collectors.toList());

        // 결과 객체 생성 및 반환
        return UploadArtworkResponse.builder().urls(fileUrls)
                .fileNames(fileNames).build();
    }

    // file 로 변환해서 multipart 올릴때 사용
    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());

        if (file.createNewFile()){
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteImage(DeleteImageRequest request) {

        UserEntity userEntity = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new YouthNotFoundException("존재하지 않는 유저id 입니다.", null));

        userEntity.deleteUserProfileImageUrl();
        String url = request.getImageUrl();

        int lastIndex = url.lastIndexOf('/');
        int secondLastIndex = url.lastIndexOf('/', lastIndex - 1);
        String fileName = url.substring(secondLastIndex + 1);
        fileUploader.delete(fileName);
    }
}
