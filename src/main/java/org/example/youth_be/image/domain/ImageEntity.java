package org.example.youth_be.image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.common.entity.BaseEntity;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(length = 1000, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String imageUploadName;

    @Column
    private Long artworkId;

    @Builder
    public ImageEntity(Long imageId, String imageUrl, String imageUploadName, Long artworkId) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.imageUploadName = imageUploadName;
        this.artworkId = artworkId;
    }

    public void setArtworkId(Long artworkId) {
        this.artworkId = artworkId;
    }
}
