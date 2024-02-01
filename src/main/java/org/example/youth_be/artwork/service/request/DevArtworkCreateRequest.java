package org.example.youth_be.artwork.service.request;

import lombok.Getter;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkStatus;

@Getter
public class DevArtworkCreateRequest {

    private String title;
    private String description;
    private ArtworkStatus artworkStatus;
    private Long userId;

    public ArtworkEntity toEntity(){
        return ArtworkEntity.builder()
                .title(this.title)
                .description(this.description)
                .artworkStatus(this.artworkStatus)
                .userId(this.userId)
                .build();
    }
}
