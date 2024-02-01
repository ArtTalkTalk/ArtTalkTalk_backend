package org.example.youth_be.artwork.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.domain.QArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkFeedType;
import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.enums.ArtworkStatus;
import org.example.youth_be.common.CursorPagingCommon;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.follow.domain.QFollowEntity;
import org.example.youth_be.like.domain.QLikeEntity;
import org.example.youth_be.user.domain.QUserEntity;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.springframework.data.domain.Slice;
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
    QFollowEntity follow = QFollowEntity.followEntity;

    @Override
    public Slice<ArtworkResponse> findByUserAndArtworkType(Long userId, Long cursorId, Integer size, ArtworkMyPageType type) {
        switch (type) {
            case ALL:
                return findAllByCondition(userId, cursorId, size);
            case SELLING:
                return findSellingsByCondition(userId, cursorId, size);
            case COLLECTION:
                return findCollectionByCondition(userId, cursorId, size);
            default:
                throw new YouthBadRequestException("지원하지 않는 파라미터 type 입니다.", null);
        }
    }

    @Override
    public Slice<ArtworkResponse> findByFeedType(Long userId, Long cursorId, Integer size, ArtworkFeedType type) {
        switch (type) {
            case ALL:
                return findAllByCondition(cursorId, size);
            case FOLLOW:
                return findFollowingByCondition(userId, cursorId, size);
            default:
                throw new YouthBadRequestException("지원하지 않는 파라미터 type 입니다.", null);
        }
    }

    private Slice<ArtworkResponse> findAllByCondition(Long cursorId, Integer size) {
        // 커서 ID를 기준으로 Artwork 조회
        List<ArtworkEntity> artworks = jpaQueryFactory
                .selectFrom(artwork)
                        .where(artwork.artworkId.lt(cursorId))
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1)
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


        return (Slice<ArtworkResponse>) CursorPagingCommon.getSlice(artworks, responses, size);
    }

    private Slice<ArtworkResponse> findFollowingByCondition(Long userId, Long cursorId, Integer size) {
        // 사용자가 팔로우한 사용자 ID 목록 조회
        List<Long> followingUserIds = jpaQueryFactory
                .select(follow.receiverId)
                .from(follow)
                .where(follow.senderId.eq(userId))
                .fetch();

        // 사용자가 팔로우한 사용자의 Artwork 조회
        List<ArtworkEntity> artworks = jpaQueryFactory
                .selectFrom(artwork)
                .where(artwork.userId.in(followingUserIds)
                        .and(artwork.artworkId.lt(cursorId))) // 커서 ID 조건
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


        return (Slice<ArtworkResponse>) CursorPagingCommon.getSlice(artworks, responses, size);
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

        return (Slice<ArtworkResponse>) CursorPagingCommon.getSlice(artworks, responses, size);
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

        return (Slice<ArtworkResponse>) CursorPagingCommon.getSlice(artworks, responses, size);
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


        return (Slice<ArtworkResponse>) CursorPagingCommon.getSlice(artworks, responses, size);
    }
}
