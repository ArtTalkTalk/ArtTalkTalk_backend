package org.example.youth_be.artwork.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.youth_be.artwork.enums.ArtworkStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ArtworkResponse {
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

    public static ArtworkResponse of(Long artworkId, String title, String description, ArtworkStatus artworkStatus, Long viewCount, Long likeCount, Long commentCount, String thumbnailImageUrl, Long artistId, String artistName, String artistProfileImageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return ArtworkResponse.builder()
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