package org.example.youth_be.artwork.service.response;

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

    public static ArtworkDetailResponse of(UserEntity userEntity, ArtworkEntity artworkEntity, List<ArtworkImageResponse> artworkImageResponse) {
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
                .build();
    }
}