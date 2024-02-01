package org.example.youth_be.artwork.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArtworkMyPageType {

    ALL("전체"),
    SELLING("판매"),
    COLLECTION("컬렉션");

    private final String description;
}
