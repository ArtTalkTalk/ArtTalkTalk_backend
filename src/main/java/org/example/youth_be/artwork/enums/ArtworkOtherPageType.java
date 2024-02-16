package org.example.youth_be.artwork.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArtworkOtherPageType {

    ALL("전체"),
    SELLING("판매");

    private final String description;
}

