package org.example.youth_be.artwork.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtworkImageResponse {
    private Long imageId;
    private String imageUrl;

    public static ArtworkImageResponse of(Long imageId, String imageUrl) {
        return ArtworkImageResponse.builder()
                .imageId(imageId)
                .imageUrl(imageUrl)
                .build();
    }
}
