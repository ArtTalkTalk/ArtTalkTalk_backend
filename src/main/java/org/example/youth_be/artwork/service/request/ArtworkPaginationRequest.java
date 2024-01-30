package org.example.youth_be.artwork.service.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtworkPaginationRequest {

    private Integer size;
    private Long lastIdxId;
}