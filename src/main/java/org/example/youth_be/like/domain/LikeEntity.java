package org.example.youth_be.like.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column( nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long artworkId;

    @Builder
    public LikeEntity(Long likeId, Long userId, Long artworkId) {
        this.likeId = likeId;
        this.userId = userId;
        this.artworkId = artworkId;
    }
}
