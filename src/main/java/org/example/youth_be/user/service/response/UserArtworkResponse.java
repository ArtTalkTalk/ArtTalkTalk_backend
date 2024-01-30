package org.example.youth_be.user.service.response;

import lombok.Builder;
import lombok.Getter;
import org.example.youth_be.artwork.enums.ArtworkStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserArtworkResponse {
    private Long artworkId;
    private String title;
    private String description;
    private ArtworkStatus artworkStatus;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private String thumbnailImageUrl;
    private Long artistId;
    private String artistName;
    private String artistProfileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserArtworkResponse of(Long artworkId, String title, String description, ArtworkStatus artworkStatus, Long viewCount, Long likeCount, Long commentCount, String thumbnailImageUrl, Long artistId, String artistName, String artistProfileImageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return UserArtworkResponse.builder()
                .artworkId(artworkId)
                .title(title)
                .description(description)
                .artworkStatus(artworkStatus)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .thumbnailImageUrl(thumbnailImageUrl)
                .artistId(artistId)
                .artistName(artistName)
                .artistProfileImageUrl(artistProfileImageUrl)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}