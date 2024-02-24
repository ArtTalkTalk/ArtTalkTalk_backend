package org.example.youth_be.artwork.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.domain.QArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.enums.ArtworkOtherPageType;
import org.example.youth_be.artwork.enums.ArtworkStatus;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.follow.domain.QFollowEntity;
import org.example.youth_be.like.domain.QLikeEntity;
import org.example.youth_be.user.domain.QUserEntity;
import org.example.youth_be.user.domain.UserEntity;
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
    public List<ArtworkResponse> findByUserAndArtworkType(Long userId, Long cursorId, Integer size, ArtworkMyPageType type) {
        switch (type) {
            case ALL:
                return findAllByCondition(userId, cursorId, size);
            case SELLING:
                return findSellingByCondition(userId, cursorId, size);
            case COLLECTION:
                return findCollectionByCondition(userId, cursorId, size);
            default:
                throw new YouthBadRequestException("지원하지 않는 파라미터 type 입니다.", null);
        }
    }

    @Override
    public List<ArtworkResponse> findByOtherUserAndArtworkType(Long userId, Long cursorId, Integer size, ArtworkOtherPageType type) {
        switch (type) {
            case ALL:
                return findAllByCondition(userId, cursorId, size);
            case SELLING:
                return findSellingByCondition(userId, cursorId, size);
            default:
                throw new YouthBadRequestException("지원하지 않는 파라미터 type 입니다.", null);
        }
    }

    @Override
    public List<ArtworkResponse> findAllFeed(Long cursorId, Integer size) {
        return findAllByCondition(cursorId, size);
    }

    @Override
    public List<ArtworkResponse> findByFollowingFeed(Long userId, Long cursorId, Integer size) {
        return findFollowingByCondition(userId, cursorId, size);
    }

    @Override
    public List<ArtworkResponse> findBySearchFeed(Long cursorId, Integer size, String keyword) {
        return findByKeyword(cursorId, size, keyword);
    }

    private BooleanExpression ltLastIdxId(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return artwork.artworkId.lt(cursorId);
    }

    private List<ArtworkResponse> findAllByCondition(Long userId, Long cursorId, Integer size) {

        UserEntity artist = getUserEntity(userId);

        // 사용자 ID와 커서 ID를 기준으로 Artwork 조회
        List<ArtworkEntity> artworks = jpaQueryFactory
                .selectFrom(artwork)
                .where(artwork.userId.eq(userId)
                        .and(ltLastIdxId(cursorId))) // 사용자 ID와 커서 ID로 필터링
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1) // hasNext 판단을 위해 size + 1개 조회
                .fetch();

        // UserArtworkResponse로 변환 (작가 정보 조회 생략)
        List<ArtworkResponse> responses = artworks.stream().limit(size+1).map(a ->
                ArtworkResponse.of(a.getArtworkId(), a.getTitle(), a.getDescription(), a.getArtworkStatus(),
                        a.getViewCount(), a.getLikeCount(), a.getCommentCount(), a.getThumbnailImageUrl(), userId,
                        artist.getNickname(), artist.getProfileImageUrl(), a.getCreatedAt(), a.getUpdatedAt())
        ).collect(Collectors.toList());

        return responses;
    }

    private List<ArtworkResponse> findSellingByCondition(Long userId, Long cursorId, Integer size) {
        UserEntity artist = getUserEntity(userId);

        // 사용자 ID와 커서 ID를 기준으로 Artwork 조회, 단, ArtworkStatus가 SELLING인 경우만
        List<ArtworkEntity> artworks = jpaQueryFactory
                .selectFrom(artwork)
                .where(artwork.userId.eq(userId)
                        .and(ltLastIdxId(cursorId)) // 커서 ID 조건
                        .and(artwork.artworkStatus.eq(ArtworkStatus.SELLING))) // ArtworkStatus가 SELLING인 경우만 필터링
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1) // 다음 페이지 존재 여부 확인을 위해 size + 1개 조회
                .fetch();

        // UserArtworkResponse로 변환
        List<ArtworkResponse> responses = artworks.stream().limit(size+1).map(a ->
                ArtworkResponse.of(a.getArtworkId(), a.getTitle(), a.getDescription(), a.getArtworkStatus(),
                        a.getViewCount(), a.getLikeCount(), a.getCommentCount(), a.getThumbnailImageUrl(), userId,
                        artist.getNickname(), artist.getProfileImageUrl(), a.getCreatedAt(), a.getUpdatedAt())
        ).collect(Collectors.toList());

        return responses;
    }

    private List<ArtworkResponse> findCollectionByCondition(Long userId, Long cursorId, Integer size) {

        // Step 1: 사용자가 좋아요한 Artwork의 ID 목록 조회
        List<Long> likedArtworkIds = jpaQueryFactory
                .select(like.artworkId)
                .from(like)
                .where(like.userId.eq(userId))
                .fetch();

        List<ArtworkResponse> responses = jpaQueryFactory
                .select(Projections.constructor(ArtworkResponse.class,
                        artwork.artworkId,
                        artwork.title,
                        artwork.description,
                        artwork.artworkStatus,
                        artwork.viewCount,
                        artwork.likeCount,
                        artwork.commentCount,
                        artwork.thumbnailImageUrl,
                        user.userId.as("artistId"),
                        user.nickname.as("artistName"),
                        user.profileImageUrl.as("artistProfileImageUrl"),
                        artwork.createdAt,
                        artwork.updatedAt))
                .from(artwork)
                .join(user).on(user.userId.eq(artwork.userId))
                .where(artwork.artworkId.in(likedArtworkIds)
                        .and(ltLastIdxId(cursorId)))
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1)
                .fetch();

        return responses;
    }


    private List<ArtworkResponse> findAllByCondition(Long cursorId, Integer size) {
        List<ArtworkResponse> responses = jpaQueryFactory
                .select(Projections.constructor(ArtworkResponse.class,
                        artwork.artworkId,
                        artwork.title,
                        artwork.description,
                        artwork.artworkStatus,
                        artwork.viewCount,
                        artwork.likeCount,
                        artwork.commentCount,
                        artwork.thumbnailImageUrl,
                        user.userId.as("artistId"),
                        user.nickname.as("artistName"),
                        user.profileImageUrl.as("artistProfileImageUrl"),
                        artwork.createdAt,
                        artwork.updatedAt))
                .from(artwork)
                .join(user).on(user.userId.eq(artwork.userId))
                .where(ltLastIdxId(cursorId))
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1)
                .fetch();

        return responses;
    }

    private List<ArtworkResponse> findFollowingByCondition(Long userId, Long cursorId, Integer size) {
        // 사용자가 팔로우한 사용자 ID 목록 조회
        List<Long> followingUserIds = jpaQueryFactory
                .select(follow.receiverId)
                .from(follow)
                .where(follow.senderId.eq(userId))
                .fetch();

        List<ArtworkResponse> responses = jpaQueryFactory
                .select(Projections.constructor(ArtworkResponse.class,
                        artwork.artworkId,
                        artwork.title,
                        artwork.description,
                        artwork.artworkStatus,
                        artwork.viewCount,
                        artwork.likeCount,
                        artwork.commentCount,
                        artwork.thumbnailImageUrl,
                        user.userId.as("artistId"),
                        user.nickname.as("artistName"),
                        user.profileImageUrl.as("artistProfileImageUrl"),
                        artwork.createdAt,
                        artwork.updatedAt))
                .from(artwork)
                .join(user).on(user.userId.eq(artwork.userId))
                .where(
                        artwork.artworkId.in(followingUserIds)
                                .and(ltLastIdxId(cursorId))
                )
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1)
                .fetch();

        return responses;
    }

    private List<ArtworkResponse> findByKeyword(Long cursorId, Integer size, String keyword) {
        List<ArtworkResponse> responses = jpaQueryFactory
                .select(Projections.constructor(ArtworkResponse.class,
                        artwork.artworkId,
                        artwork.title,
                        artwork.description,
                        artwork.artworkStatus,
                        artwork.viewCount,
                        artwork.likeCount,
                        artwork.commentCount,
                        artwork.thumbnailImageUrl,
                        user.userId.as("artistId"),
                        user.nickname.as("artistName"),
                        user.profileImageUrl.as("artistProfileImageUrl"),
                        artwork.createdAt,
                        artwork.updatedAt))
                .from(artwork)
                .join(user).on(user.userId.eq(artwork.userId))
                .where(artwork.title.contains(keyword)
                        .or(artwork.description.contains(keyword))
                        .and(ltLastIdxId(cursorId)))
                .orderBy(artwork.artworkId.desc())
                .limit(size + 1)
                .fetch();

        return responses;
    }

    private UserEntity getUserEntity(Long userId) {
        UserEntity artist = jpaQueryFactory
                .selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchOne();
        return artist;
    }
}
