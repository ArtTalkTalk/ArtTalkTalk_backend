package org.example.youth_be.artwork.service.request;

import lombok.Getter;

@Getter
public class ArtworkPaginationRequest {

    private Integer size;
    private Long lastIdxId;
}