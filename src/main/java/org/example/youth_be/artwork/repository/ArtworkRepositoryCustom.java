package org.example.youth_be.artwork.repository;

import org.example.youth_be.user.service.response.UserArtworkResponse;
import org.springframework.data.domain.Slice;

public interface ArtworkRepositoryCustom {
    // 유저 마이페이지에서 전체 작품 조회
    Slice<UserArtworkResponse> findAllByCondition(Long userId, Long cursorId, Integer size);

    // 유저 마이페이지에서 판매중 작품 조회
    Slice<UserArtworkResponse> findSellingsByCondition(Long userId, Long cursorId, Integer size);

    // 유저 마이페이지에서 유저가 좋아요한 작품 조회
    Slice<UserArtworkResponse> findLikedByCondition(Long userId, Long cursorId, Integer size);

}
