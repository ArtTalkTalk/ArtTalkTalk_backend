package org.example.youth_be.artwork.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.artwork.enums.ArtworkStatus;

@Entity
@Table(name = "Artwork")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtworkEntity {
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

    private Long userId;

    @Builder
    public ArtworkEntity(Long artworkId, String title, String description, ArtworkStatus artworkStatus, Long viewCount, Long likeCount, Long commentCount, Long userId) {
        this.artworkId = artworkId;
        this.title = title;
        this.description = description;
        this.artworkStatus = artworkStatus;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.userId = userId;
    }

}
