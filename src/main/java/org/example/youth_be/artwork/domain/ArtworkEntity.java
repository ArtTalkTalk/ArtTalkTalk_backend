package org.example.youth_be.artwork.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.artwork.enums.ArtworkStatus;
import org.example.youth_be.common.converter.LongListConverter;
import org.example.youth_be.common.entity.BaseEntity;

import java.util.List;

@Entity
@Table(name = "Artwork")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtworkEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artworkId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtworkStatus artworkStatus;

    private Long viewCount;

    private Long likeCount;

    private Long commentCount;

    private String thumbnailImageUrl;

    private Long userId;

    @Convert(converter = LongListConverter.class)
    private List<Long> imageIdList;

    @Builder
    public ArtworkEntity(Long artworkId, String title, String description, ArtworkStatus artworkStatus, Long viewCount, Long likeCount, Long commentCount, String thumbnailImageUrl, Long userId, List<Long> imageIdList) {
        this.artworkId = artworkId;
        this.title = title;
        this.description = description;
        this.artworkStatus = artworkStatus;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.userId = userId;
        this.imageIdList = imageIdList;
    }

    public void setCount(){
        this.viewCount = 0L;
        this.likeCount = 0L;
        this.commentCount = 0L;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl){
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public void updateArtwork(String title, String description, ArtworkStatus artworkStatus, List<Long> imageIdList, String thumbnailImageUrl){
        this.title = title;
        this.description = description;
        this.artworkStatus = artworkStatus;
        this.imageIdList = imageIdList;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
