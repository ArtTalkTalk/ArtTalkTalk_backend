package org.example.youth_be.artwork.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.artwork.service.request.ArtworkCreateRequest;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.request.ArtworkUpdateRequest;
import org.example.youth_be.artwork.service.response.ArtworkDetailResponse;
import org.example.youth_be.artwork.service.response.ArtworkImageResponse;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.comment.domain.CommentEntity;
import org.example.youth_be.comment.repository.CommentRepository;
import org.example.youth_be.common.CursorPagingCommon;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.aop.TransactionalDistributedLock;
import org.example.youth_be.common.enums.LockUsageType;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.follow.domain.FollowEntity;
import org.example.youth_be.follow.repository.FollowRepository;
import org.example.youth_be.image.domain.ImageEntity;
import org.example.youth_be.image.repository.ImageRepository;
import org.example.youth_be.like.domain.LikeEntity;
import org.example.youth_be.like.repository.LikeRepository;
import org.example.youth_be.s3.service.FileUploader;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.youth_be.common.s3.FileNameExtractor.getFileName;
import static org.example.youth_be.common.util.DebuggingTemplate.NotAuthorized;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final FollowRepository followRepository;
    private final FileUploader fileUploader;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @TransactionalDistributedLock(key = "#tokenClaim.getUserId()", usage = LockUsageType.ARTWORK)
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
    public PageResponse<ArtworkResponse> getArtworks(ArtworkPaginationRequest request) {
        Integer size = request.getSize();
        Long cursorId = request.getLastIdxId();

        List<ArtworkResponse> responses = artworkRepository.findAllFeed(cursorId, size);
        Slice<ArtworkResponse> artworkResponses = CursorPagingCommon.getSlice(responses, size);
        return PageResponse.of(artworkResponses);
    }

    @Transactional(readOnly = true)
    public PageResponse<ArtworkResponse> getArtworksFollowing(TokenClaim tokenClaim, ArtworkPaginationRequest request) {
        Integer size = request.getSize();
        Long cursorId = request.getLastIdxId();
        Long userId = tokenClaim.getUserId();

        List<ArtworkResponse> responses = artworkRepository.findByFollowingFeed(userId, cursorId, size);
        Slice<ArtworkResponse> artworkResponses = CursorPagingCommon.getSlice(responses, size);
        return PageResponse.of(artworkResponses);
    }

    @Transactional
    public ArtworkDetailResponse getArtwork(TokenClaim claim, Long artworkId) {
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 작품를 찾을 수 없습니다.", null));

        // 작가 entity 조회
        UserEntity userEntity = userRepository.findById(artworkEntity.getUserId()).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));

        // TokenClaim이 null이 아닐 때만 getLikeId 호출
        Long likeId = (claim != null) ? getLikeId(claim, artworkId) : null;

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

        // TokenClaim이 null이 아닐 때만 getFollowId 호출
        Long followId = (claim != null) ? getFollowId(claim, userEntity.getUserId()) : null;

        artworkEntity.increaseViewCount();
        return ArtworkDetailResponse.of(userEntity, artworkEntity, artworkImageResponses, likeId, followId);
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
                String imageUrl = image.getImageUrl();
                String fileName = getFileName(imageUrl);
                deleteImageFileName.add(fileName);

                // 이미지 삭제
                imageRepository.delete(image);
            }
        }

        List<Long> imageIds = request.getImageIds(); // 요청에서 이미지 ID 목록을 가져옵니다.
        List<ImageEntity> images = imageRepository.findAllById(imageIds); // ID 목록에 해당하는 ImageEntity 객체들을 조회합니다.

        // 조회된 ImageEntity 객체들을 원래 ID 목록의 순서대로 정렬합니다.
        Map<Long, ImageEntity> imageMap = images.stream()
                .collect(Collectors.toMap(ImageEntity::getImageId, Function.identity()));

        List<ImageEntity> sortedImages = imageIds.stream()
                .map(imageMap::get)
                .collect(Collectors.toList());

        log.info(String.valueOf(sortedImages.get(0).getArtworkId()));

        // 이미지에 artworkId update
        for (ImageEntity image : sortedImages) {
            image.setArtworkId(artworkId);
        }

        // artwork update(썸네일, 이미지 배열)
        artworkEntity.updateArtwork(request.getTitle(),
                request.getDescription(),
                request.getArtworkStatus(),
                request.getImageIds(),
                sortedImages.get(0).getImageUrl());

        // s3 상의 이미지 삭제
        for (String fileName : deleteImageFileName) {
            fileUploader.delete(fileName);
        }
    }

    private Long getFollowId(TokenClaim claim, Long receiverId) {
        if (claim == null) {
            return null;
        }
        FollowEntity followEntity = followRepository.findBySenderIdAndReceiverId(claim.getUserId(), receiverId).orElse(null);
        if (followEntity == null) {
            return null;
        }
        return followEntity.getFollowId();
    }
    
    private Long getLikeId(TokenClaim claim, Long artworkId) {
        // tokenClaim이 없을 경우 null 반환
        Long userId = Optional.ofNullable(claim)
                .map(TokenClaim::getUserId)
                .orElse(null);

        Long likeId = null;
        if (userId != null) {
            LikeEntity likeEntity = likeRepository.findByArtworkIdAndUserId(artworkId, userId).orElse(null);
            if (likeEntity != null) {
                likeId = likeEntity.getLikeId();
            }
        }
        return likeId;
    }

    @Transactional
    public void deleteArtwork(TokenClaim tokenClaim, Long artworkId) {
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 작품을 찾을 수 없습니다.", null));
        Long userId = artworkEntity.getUserId();

        if (tokenClaim.isNotAuthorized(userId)) {
            throw new YouthBadRequestException("권한이 없는 사용자입니다.", NotAuthorized(userId, tokenClaim));
        }

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));

        List<Long> imageIdList = artworkEntity.getImageIdList();
        List<ImageEntity> images = imageRepository.findAllById(imageIdList);
        List<CommentEntity> comments = commentRepository.findByArtworkId(artworkId);

        // 유저 좋아요 개수 업데이트
        userEntity.updateTotalLikeCount(artworkEntity.getLikeCount());
        // 댓글 삭제
        commentRepository.deleteAllInBatch(comments);
        // 이미지 삭제
        imageRepository.deleteAllInBatch(images);
        // 작품 삭제
        artworkRepository.delete(artworkEntity);
    }

    @Transactional
    public PageResponse<ArtworkResponse> searchArtwork(String keyword, ArtworkPaginationRequest request) {
        Integer size = request.getSize();
        Long cursorId = request.getLastIdxId();

        List<ArtworkResponse> responses = artworkRepository.findBySearchFeed(cursorId, size, keyword);
        Slice<ArtworkResponse> artworkResponses = CursorPagingCommon.getSlice(responses, size);
        return PageResponse.of(artworkResponses);
    }
}
