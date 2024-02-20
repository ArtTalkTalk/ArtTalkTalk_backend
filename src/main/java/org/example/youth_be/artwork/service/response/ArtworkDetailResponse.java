package org.example.youth_be.artwork.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkStatus;
import org.example.youth_be.user.domain.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ArtworkDetailResponse {
    private Long artworkId;
    private String title;
    private String description;
    private ArtworkStatus artworkStatus;
    private List<ArtworkImageResponse> artworkImageResponse;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private String thumbnailImageUrl;
    private Long artistId;
    private String artistName;
    private String artistProfileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeId;
    @Schema(description = "작가 follow Id, 팔로우하지 않았다면 null입니다")
    private Long followId;

    public static ArtworkDetailResponse of(UserEntity userEntity, ArtworkEntity artworkEntity, List<ArtworkImageResponse> artworkImageResponse, Long likeId, Long followId) {
        return ArtworkDetailResponse.builder()
                .artworkId(artworkEntity.getArtworkId())
                .title(artworkEntity.getTitle())
                .description(artworkEntity.getDescription())
                .artworkStatus(artworkEntity.getArtworkStatus())
                .artworkImageResponse(artworkImageResponse)
                .viewCount(artworkEntity.getViewCount())
                .likeCount(artworkEntity.getLikeCount())
                .commentCount(artworkEntity.getCommentCount())
                .thumbnailImageUrl(artworkEntity.getThumbnailImageUrl())
                .artistId(userEntity.getUserId())
                .artistName(userEntity.getNickname())
                .artistProfileImageUrl(userEntity.getProfileImageUrl())
                .createdAt(artworkEntity.getCreatedAt())
                .updatedAt(artworkEntity.getUpdatedAt())
                .likeId(likeId)
                .followId(followId)
                .build();
    }
}