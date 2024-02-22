package org.example.youth_be.like.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.like.domain.LikeEntity;
import org.example.youth_be.like.repository.LikeRepository;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    
    private final ArtworkRepository artworkRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CreateLikeResponse createArtworkLike(Long artworkId, TokenClaim tokenClaim) {
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId).orElseThrow(() -> new YouthNotFoundException("작품을 찾을 수 없습니다.", null));
        UserEntity userEntity = userRepository.findById(artworkEntity.getUserId()).orElseThrow(() -> new YouthNotFoundException("작품의 유저를 찾을 수 없습니다.", null));

        LikeEntity likeEntity = LikeEntity.builder()
                .userId(tokenClaim.getUserId())
                .artworkId(artworkId)
                .build();
        likeRepository.save(likeEntity);

        userEntity.increaseTotalLikeCount();
        artworkEntity.increaseLikeCount();
        CreateLikeResponse createLikeResponse = CreateLikeResponse.builder().likeId(likeEntity.getLikeId()).build();
        return createLikeResponse;
    }
    
    @Transactional
    public void deleteArtworkLike(Long artworkId, Long likeId, TokenClaim tokenClaim) {
        LikeEntity likeEntity = likeRepository.findById(likeId).orElseThrow(() -> new YouthNotFoundException("좋아요를 찾을 수 없습니다.", null));
        validateLikeOwner(tokenClaim, likeEntity);
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId).orElseThrow(() -> new YouthNotFoundException("작품을 찾을 수 없습니다.", null));
        UserEntity userEntity = userRepository.findById(artworkEntity.getUserId()).orElseThrow(() -> new YouthNotFoundException("작품의 유저를 찾을 수 없습니다.", null));

        likeRepository.delete(likeEntity);
        userEntity.decreaseTotalLikeCount();
        artworkEntity.decreaseLikeCount();
    }

    private void validateLikeOwner(TokenClaim tokenClaim, LikeEntity likeEntity) {
        if (!likeEntity.getUserId().equals(tokenClaim.getUserId())) {
            throw new YouthNotFoundException("좋아요 취소는 등록자만 가능합니다.", null);
        }
    }
}
