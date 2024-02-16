package org.example.youth_be.artwork.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArtworkStatus {

    PUBLIC("PUBLIC", "게시용"),
    SELLING("SELLING", "판매"),
    FREE("FREE","무료나눔");

    private final String key;
    private final String description;
}