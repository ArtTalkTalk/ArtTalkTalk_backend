package org.example.youth_be.artwork.service.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtworkSearchRequest {
    private Integer size;
    private Long lastIdxId;
    private String keyword;
}
