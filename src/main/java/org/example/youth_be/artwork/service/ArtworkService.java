package org.example.youth_be.artwork.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkFeedType;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.artwork.service.request.ArtworkCreateRequest;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.request.ArtworkUpdateRequest;
import org.example.youth_be.artwork.service.response.ArtworkDetailResponse;
import org.example.youth_be.artwork.service.response.ArtworkImageResponse;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.CursorPagingCommon;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.image.domain.ImageEntity;
import org.example.youth_be.image.repository.ImageRepository;
import org.example.youth_be.s3.service.FileUploader;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.youth_be.common.s3.FileNameExtractor.getFileName;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final FileUploader fileUploader;

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

        List<Long> ids = artworkEntity.getImageIdList();
        List<ImageEntity> images = imageRepository.findAllById(ids);

        // ID 순서대로 정렬
        List<ImageEntity> sortedImages = ids.stream()
                .flatMap(id -> images.stream().filter(image -> image.getImageId().equals(id)))
                .collect(Collectors.toList());

        // 응답 구성
        List<ArtworkImageResponse> artworkImageResponses = sortedImages.stream()
                .map(image -> ArtworkImageResponse.of(image.getImageId(), image.getImageUrl()))
                .collect(Collectors.toList());

        return ArtworkDetailResponse.of(userEntity, artworkEntity, artworkImageResponses);
    }

    @Transactional
    public void updateArtwork(TokenClaim tokenClaim, Long artworkId, ArtworkUpdateRequest request) {
        // 작품 수정 권한 확인(작품 작성 유저와 수정 요청 유저가 동일한지 확인)
        Long userId = tokenClaim.getUserId();
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 작품을 찾을 수 없습니다.", null));
        if (!artworkEntity.getUserId().equals(userId)) {
            throw new YouthBadRequestException("작품을 수정할 권한이 없습니다.", null);
        }

        // 요청 배열(이미지 순서 업데이트)에 없는 이미지 DB에서 삭제
        List<ImageEntity> deleteImages = imageRepository.findByArtworkId(artworkId);
        List<String> deleteImageFileName = new ArrayList<>(); // s3에서 지우기 위한 배열
        for (ImageEntity image : deleteImages) {
            if (!request.getImageIds().contains(image.getImageId())) {

                // fileName 추출
                String fileName = getFileName(image);
                deleteImageFileName.add(fileName);

                // 이미지 삭제
                imageRepository.delete(image);
            }
        }

        // 이미지에 artworkId update
        List<ImageEntity> images = imageRepository.findAllById(request.getImageIds());
        for (ImageEntity image : images) {
            image.setArtworkId(artworkId);
        }

        // artwork update(썸네일, 이미지 배열)
        artworkEntity.updateArtwork(request.getTitle(),
                request.getDescription(),
                request.getArtworkStatus(),
                request.getImageIds(),
                images.get(0).getImageUrl());

        // s3 상의 이미지 삭제
        for (String fileName : deleteImageFileName) {
            fileUploader.delete(fileName);
        }
    }
}
