package org.example.youth_be.artwork.service.request;

import lombok.Getter;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkStatus;

@Getter
public class DevArtworkCreateRequest {

    private String title;
    private String description;
    private ArtworkStatus artworkStatus;
    private Long viewCount = 0L;
    private Long likeCount;
    private Long commentCount;
    private Long userId = 1L;

    public ArtworkEntity toEntity(){
        return ArtworkEntity.builder()
                .title(this.title)
                .description(this.description)
                .artworkStatus(this.artworkStatus)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .userId(this.userId)
                .build();
    }
}
