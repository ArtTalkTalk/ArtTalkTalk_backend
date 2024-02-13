package org.example.youth_be.artwork.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkFeedType;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.artwork.service.request.ArtworkCreateRequest;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.response.ArtworkDetailResponse;
import org.example.youth_be.artwork.service.response.ArtworkImageResponse;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.CursorPagingCommon;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.image.domain.ImageEntity;
import org.example.youth_be.image.repository.ImageRepository;
import org.example.youth_be.image.service.ImageService;
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
    public Long createArtwork(TokenClaim tokenClaim, ArtworkCreateRequest request) {

        ArtworkEntity artworkEntity = ArtworkEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .artworkStatus(request.getArtworkStatus())
                .viewCount(0L)
                .likeCount(0L)
                .commentCount(0L)
                .userId(tokenClaim.getUserId())
                .imageIdList(request.getImageIds())
                .build();
        artworkRepository.save(artworkEntity);

        request.getImageIds().forEach(imageId -> {
            ImageEntity imageEntity = imageRepository.findById(imageId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 이미지를 찾을 수 없습니다.", null));
            imageEntity.setArtworkId(artworkEntity.getArtworkId());
        });

        // 썸네일 이미지 설정
        artworkEntity.setThumbnailImageUrl(imageRepository.findByArtworkId(artworkEntity.getArtworkId()).get(0).getImageUrl());

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

        List<ArtworkImageResponse> artworkImageResponses = images.stream()
                .map(image -> ArtworkImageResponse.of(image.getImageId(), image.getImageUrl()))
                .collect(Collectors.toList());

        return ArtworkDetailResponse.of(userEntity, artworkEntity, artworkImageResponses);
    }
}
