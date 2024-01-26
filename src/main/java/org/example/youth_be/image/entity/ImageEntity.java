package org.example.youth_be.image.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@AllArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_url", length = 1000, nullable = false)
    private String imageUrl;

    @Column(name = "image_upload_name", nullable = false)
    private String imageUploadName;

    @Column(name = "artwork_id", nullable = false)
    private Long artworkId;
}
