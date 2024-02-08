package org.example.youth_be.user.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum{
    ASSOCIATE("준회원"),
    REGULAR("정회원");

    private final String description;

    UserRoleEnum(String description) {
        this.description = description;
    }

}
