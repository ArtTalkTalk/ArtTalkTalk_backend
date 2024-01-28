package org.example.youth_be.artwork.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArtworkStatus {

    PUBLIC("공개"),
    SELLING("판매중"),
    FREE("무료나눔"),
    PRIVATE("나만 보기");

    private final String description;
}