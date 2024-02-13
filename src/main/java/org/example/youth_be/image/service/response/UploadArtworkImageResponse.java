package org.example.youth_be.image.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadArtworkImageResponse {
    private Long imageId;
    private String imageUrl;

    public static UploadArtworkImageResponse of(String imageUrl, Long imageId) {
        return UploadArtworkImageResponse.builder()
                .imageId(imageId)
                .imageUrl(imageUrl)
                .build();
    }
}