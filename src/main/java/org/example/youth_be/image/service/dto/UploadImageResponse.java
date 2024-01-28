package org.example.youth_be.image.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadImageResponse {
    private String imageUrl;

    public static UploadImageResponse of(String imageUrl) {
        return UploadImageResponse.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
