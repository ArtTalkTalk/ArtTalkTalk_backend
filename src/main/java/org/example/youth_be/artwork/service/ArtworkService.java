package org.example.youth_be.artwork.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkFeedType;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.artwork.service.request.ArtworkCreateRequest;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.response.ArtworkDetailResponse;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.CursorPagingCommon;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.image.domain.ImageEntity;
import org.example.youth_be.image.repository.ImageRepository;
import org.example.youth_be.image.service.ImageService;
import org.example.youth_be.image.service.response.UploadArtworkResponse;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Transactional
    public Long createArtwork(ArtworkCreateRequest request) {
        ArtworkEntity artworkEntity = ArtworkEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .artworkStatus(request.getArtworkStatus())
                .viewCount(0L)
                .likeCount(0L)
                .commentCount(0L)
                .userId(request.getUserId())
                .build();
        artworkRepository.save(artworkEntity);

        // 이미지 업로드
        UploadArtworkResponse uploadArtworkResponse = imageService.uploadImages(request.getImages());

        // 이미지로 변환
        List<ImageEntity> imageEntities = uploadArtworkResponse.getUrls().stream()
                .map(url -> ImageEntity.builder()
                        .imageUrl(url)
                        .imageUploadName(url.substring(url.lastIndexOf('/') + 1))
                        .artworkId(artworkEntity.getArtworkId())
                        .build())
                .collect(Collectors.toList());

        // 이미지 저장
        imageRepository.saveAll(imageEntities);

        // 썸네일 이미지 설정
        artworkEntity.setThumbnailImageUrl(imageEntities.get(0).getImageUrl());

        return artworkEntity.getArtworkId();
    }

    @Transactional(readOnly = true)
    public PageResponse<ArtworkResponse> getArtworks(Long userId, ArtworkFeedType type, ArtworkPaginationRequest request) {
        Integer size = request.getSize();
        Long cursorId = request.getLastIdxId();

        List<ArtworkResponse> responses = artworkRepository.findByFeedType(userId, cursorId, size, type);
        Slice<ArtworkResponse> artworkResponses = CursorPagingCommon.getSlice(responses, size);
        return PageResponse.of(artworkResponses);
    }

    @Transactional(readOnly = true)
    public ArtworkDetailResponse getArtwork(Long artworkId) {
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 작품를 찾을 수 없습니다.", null));
        UserEntity userEntity = userRepository.findById(artworkEntity.getUserId()).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        List<ImageEntity> images = imageRepository.findByArtworkId(artworkId);
        List<String> imageUrls = images.stream().map(ImageEntity::getImageUrl).toList();
        return ArtworkDetailResponse.of(userEntity, artworkEntity, imageUrls);
    }
}
