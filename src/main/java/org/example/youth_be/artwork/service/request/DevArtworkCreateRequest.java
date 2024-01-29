package org.example.youth_be.artwork.service.request;

import lombok.Getter;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkStatus;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Getter
public class DevArtworkCreateRequest {

    private final String title = UUID.randomUUID().toString() + "작품 제목";
    private final String description = UUID.randomUUID().toString() + "작품 설명";
    private final ArtworkStatus artworkStatus = ArtworkStatus.PUBLIC;
    private final Long viewCount = Math.abs(RandomGenerator.getDefault().nextLong() % 100);
    private final Long likeCount = Math.abs(RandomGenerator.getDefault().nextLong() % 100);
    private final Long commentCount = Math.abs(RandomGenerator.getDefault().nextLong() % 100);
    private final Long userId = 1L;

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
