package org.example.youth_be.artwork.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArtworkFeedType {

    ALL("전체"),
    FOLLOW("팔로잉");

    private final String description;
}
