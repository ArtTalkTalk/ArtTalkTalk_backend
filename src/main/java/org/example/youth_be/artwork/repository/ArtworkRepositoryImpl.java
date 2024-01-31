package org.example.youth_be.artwork.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.domain.QArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkStatus;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.like.domain.QLikeEntity;
import org.example.youth_be.user.domain.QUserEntity;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.enums.ArtworkType;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ArtworkRepositoryImpl implements ArtworkRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QArtworkEntity artwork = QArtworkEntity.artworkEntity;
    QUserEntity user = QUserEntity.userEntity;
    QLikeEntity like = QLikeEntity.likeEntity;

    @Override
    public Slice<ArtworkResponse> findByUserAndArtworkType(Long userId, Long cursorId, Integer size, ArtworkType type) {
        switch (type) {
            case ALL:
                return findAllByCondition(userId, cursorId, size);
            case SELLING:
                return findSellingsByCondition(userId, cursorId, size);
            case COLLECTION:
                return findCollectionByCondition(userId, cursorId, size);
            default:
                throw new YouthNotFoundException("작품을 찾을 수 없습니다.", null);
        }
    }

    private Slice<ArtworkResponse> findAllByCondition(Long userId, Long cursorId, Integer size) {

        UserEntity artist = jpaQueryFactory
                .selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchOne();

        // 사용자 ID와 커서 ID를 기준으로 Artwork 조회
        List<ArtworkEntity> artworks = jpaQueryFactory
                .selectFrom(artwork)
                .where(artwork.userId.eq(userId)
                        .and(artwork.artworkId.lt(cursorId))) // 사용자 ID와 커서 ID로 필터링
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1) // hasNext 판단을 위해 size + 1개 조회
                .fetch();

        // UserArtworkResponse로 변환 (작가 정보 조회 생략)
        List<ArtworkResponse> responses = artworks.stream().limit(size).map(a ->
                ArtworkResponse.of(a.getArtworkId(), a.getTitle(), a.getDescription(), a.getArtworkStatus(),
                        a.getViewCount(), a.getLikeCount(), a.getCommentCount(), a.getThumbnailImageUrl(), userId,
                        artist.getNickname(), artist.getProfileImageUrl(), a.getCreatedAt(), a.getUpdatedAt())).collect(Collectors.toList());

        boolean hasNext = artworks.size() > size;

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, size);

        // SliceImpl 객체로 반환
        return new SliceImpl<>(responses, pageable, hasNext);
    }

    private Slice<ArtworkResponse> findSellingsByCondition(Long userId, Long cursorId, Integer size) {
        UserEntity artist = jpaQueryFactory
                .selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchOne();

        // 사용자 ID와 커서 ID를 기준으로 Artwork 조회, 단, ArtworkStatus가 SELLING인 경우만
        List<ArtworkEntity> artworks = jpaQueryFactory
                .selectFrom(artwork)
                .where(artwork.userId.eq(userId)
                        .and(artwork.artworkId.lt(cursorId)) // 커서 ID 조건
                        .and(artwork.artworkStatus.eq(ArtworkStatus.SELLING))) // ArtworkStatus가 SELLING인 경우만 필터링
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1) // 다음 페이지 존재 여부 확인을 위해 size + 1개 조회
                .fetch();

        // UserArtworkResponse로 변환
        List<ArtworkResponse> responses = artworks.stream().limit(size).map(a ->
                ArtworkResponse.of(a.getArtworkId(), a.getTitle(), a.getDescription(), a.getArtworkStatus(),
                        a.getViewCount(), a.getLikeCount(), a.getCommentCount(), a.getThumbnailImageUrl(), userId,
                        artist.getNickname(), artist.getProfileImageUrl(), a.getCreatedAt(), a.getUpdatedAt())
        ).collect(Collectors.toList());

        boolean hasNext = artworks.size() > size; // 조회된 목록의 크기가 요청된 size보다 크면 다음 페이지가 존재

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, size);

        // SliceImpl 객체로 반환
        return new SliceImpl<>(responses, pageable, hasNext);
    }


    private Slice<ArtworkResponse> findCollectionByCondition(Long userId, Long cursorId, Integer size) {

        // Step 1: 사용자가 좋아요한 Artwork의 ID 목록 조회
        List<Long> likedArtworkIds = jpaQueryFactory
                .select(like.artworkId)
                .from(like)
                .where(like.userId.eq(userId))
                .fetch();

        // Step 2: 좋아요한 Artwork 조회
        List<ArtworkEntity> artworks = jpaQueryFactory
                .selectFrom(artwork)
                .where(artwork.artworkId.in(likedArtworkIds)
                        .and(artwork.artworkId.lt(cursorId)))
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1) // hasNext 판단을 위해 size + 1개 조회
                .fetch();

        // UserArtworkResponse로 변환
        List<ArtworkResponse> responses = artworks.stream().limit(size).map(a -> {
            // UserEntity 조회
            UserEntity artist = jpaQueryFactory
                    .selectFrom(user)
                    .where(user.userId.eq(a.getUserId()))
                    .fetchOne();

            return ArtworkResponse.of(a.getArtworkId(), a.getTitle(), a.getDescription(), a.getArtworkStatus(),
                    a.getViewCount(), a.getLikeCount(), a.getCommentCount(), a.getThumbnailImageUrl(), a.getUserId(),
                    artist.getNickname(), artist.getProfileImageUrl(), a.getCreatedAt(), a.getUpdatedAt());
        }).collect(Collectors.toList());


        boolean hasNext = artworks.size() > size;

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, size);

        // SliceImpl 객체로 반환
        return new SliceImpl<>(responses, pageable, hasNext);
    }
}
