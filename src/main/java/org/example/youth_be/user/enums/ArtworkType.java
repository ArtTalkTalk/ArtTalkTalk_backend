package org.example.youth_be.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArtworkType {

    ALL("전체"),SELLING("판매중"),COLLECTION("컬렉션");

    private final String description;

}
