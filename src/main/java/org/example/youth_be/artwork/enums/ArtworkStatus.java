package org.example.youth_be.artwork.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArtworkStatus {

    PUBLIC("게시용"),
    SELLING("판매"),
    FREE("무료나눔");

    private final String description;
}