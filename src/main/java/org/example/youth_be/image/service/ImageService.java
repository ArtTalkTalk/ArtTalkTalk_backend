package org.example.youth_be.image.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.s3.FileNameExtractor;
import org.example.youth_be.image.domain.ImageEntity;
import org.example.youth_be.image.enums.ImageType;
import org.example.youth_be.image.repository.ImageRepository;
import org.example.youth_be.image.service.request.DeleteImageRequest;
import org.example.youth_be.image.service.request.ImageUploadRequest;
import org.example.youth_be.image.service.response.UploadArtworkImageResponse;
import org.example.youth_be.image.service.response.UploadImageResponse;
import org.example.youth_be.s3.service.FileUploader;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String imageUrl = fileUploader.upload(request.getFile(), ImageType.ARTWORK);
        String fileName = FileNameExtractor.getFileName(imageUrl);
        ImageEntity imageEntity = ImageEntity.builder()
                .imageUrl(imageUrl)
                .imageUploadName(fileName)
                .build();
        imageRepository.save(imageEntity);
        return UploadArtworkImageResponse.of(imageUrl, imageEntity.getImageId());
    }

    @Transactional
    public void deleteImage(DeleteImageRequest request) {

        UserEntity userEntity = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new YouthNotFoundException("존재하지 않는 유저id 입니다.", null));

        userEntity.deleteUserProfileImageUrl();
        String url = request.getImageUrl();

        String fileName = FileNameExtractor.getFileName(url);
        fileUploader.delete(fileName);
    }
}
